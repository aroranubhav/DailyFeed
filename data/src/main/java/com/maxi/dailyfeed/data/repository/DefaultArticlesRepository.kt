package com.maxi.dailyfeed.data.repository

import com.maxi.dailyfeed.common.CacheStatus
import com.maxi.dailyfeed.common.DBSyncMetaData
import com.maxi.dailyfeed.common.DispatcherProvider
import com.maxi.dailyfeed.common.ErrorType
import com.maxi.dailyfeed.common.Resource
import com.maxi.dailyfeed.data.common.safeApiCall
import com.maxi.dailyfeed.data.common.safeDbCall
import com.maxi.dailyfeed.data.mappers.asDomainList
import com.maxi.dailyfeed.data.mappers.toDomainList
import com.maxi.dailyfeed.data.mappers.toEntityList
import com.maxi.dailyfeed.data.source.local.ArticleLocalDataSource
import com.maxi.dailyfeed.data.source.remote.ArticleRemoteDataSource
import com.maxi.dailyfeed.domain.model.Article
import com.maxi.dailyfeed.domain.repository.ArticlesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

class DefaultArticlesRepository @Inject constructor(
    private val remote: ArticleRemoteDataSource,
    private val local: ArticleLocalDataSource,
    private val dispatchers: DispatcherProvider
) : ArticlesRepository {

    override suspend fun fetchAndCacheNewsArticles(
        forceRefresh: Boolean,
        language: String,
        country: String
    ): Resource<List<Article>> {
        val apiResult = safeApiCall {
            remote.getNewsArticle(forceRefresh, language, country)
        }

        return when (apiResult) {
            is Resource.Success -> {
                val articles = apiResult.data.articles
                val articlesEntities = articles.toEntityList()
                val domainArticles = articles.asDomainList()

                val dbResult = safeDbCall {
                    local.insertNewsArticles(articlesEntities)
                }

                when (dbResult) {
                    //store sync time to db
                    is Resource.Success -> {
                        Resource.Success(
                            articlesEntities.toDomainList(),
                            syncMetaData = DBSyncMetaData(
                                isCached = true,
                                cacheStatus = CacheStatus.SYNCED
                            )
                        )
                    }

                    is Resource.Error -> {
                        //sync in background
                        Resource.Success(
                            domainArticles,
                            syncMetaData = DBSyncMetaData(
                                isCached = false,
                                cacheStatus = CacheStatus.SYNCING,
                                cacheError = "Failed to sync, ${dbResult.message}.\n Retrying syncing in the background."
                            )
                        )
                    }

                    is Resource.NoChange,
                    is Resource.Loading -> {
                        Resource.Error(
                            ErrorType.DATABASE,
                            "Unexpected error occurred!"
                        )
                    }
                }
            }

            is Resource.Error -> {
                apiResult
            }

            is Resource.NoChange -> {
                handleNoChange()
            }

            is Resource.Loading -> {
                Resource.Error(
                    ErrorType.DATABASE,
                    "Unexpected error occurred!"
                )
            }
        }
    }

    private suspend fun handleNoChange(): Resource<List<Article>> {
        val articles = local.getNewsArticlesSnapshot()

        return Resource.Success(
            articles.toDomainList(),
            syncMetaData = DBSyncMetaData(
                isCached = true,
                cacheStatus = CacheStatus.SYNCED,
                notNotified = true
            )
        )
    }

    override fun observeNewsArticles(): Flow<Resource<List<Article>>> =
        local.getNewsArticles()
            .map { entities ->
                Resource.Success(entities.toDomainList()) as Resource<List<Article>>
            }
            .onStart {
                emit(Resource.Loading)
            }
            .catch { e ->
                emit(
                    Resource.Error(
                        ErrorType.DATABASE,
                        e.message
                    )
                )
            }
            .flowOn(dispatchers.io)
}