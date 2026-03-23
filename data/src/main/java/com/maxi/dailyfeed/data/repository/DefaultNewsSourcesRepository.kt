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
import com.maxi.dailyfeed.data.source.local.NewsSourceLocalDataSource
import com.maxi.dailyfeed.data.source.remote.NewsSourceRemoteDataSource
import com.maxi.dailyfeed.domain.model.NewsSource
import com.maxi.dailyfeed.domain.repository.NewsSourcesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

class DefaultNewsSourcesRepository @Inject constructor(
    private val local: NewsSourceLocalDataSource,
    private val remote: NewsSourceRemoteDataSource,
    private val dispatchers: DispatcherProvider
) : NewsSourcesRepository {

    override suspend fun fetchAndCacheNewsSources(forceRefresh: Boolean): Resource<List<NewsSource>> {
        val apiResult = safeApiCall {
            remote.getNewsSources(forceRefresh)
        }

        return when (apiResult) {
            is Resource.Success -> {
                val sourcesEntities = apiResult.data.sources.toEntityList()
                val sources = apiResult.data.sources.asDomainList()

                val dbResult = safeDbCall {
                    local.insertNewsSources(sourcesEntities)
                }

                when (dbResult) {
                    is Resource.Success -> {
                        //appDataStore.saveSourcesFetchTimestamp(System.currentTimeMillis())
                        Resource.Success(
                            sources,
                            syncMetaData = DBSyncMetaData(
                                isCached = true,
                                cacheStatus = CacheStatus.SYNCED
                            )
                        )
                    }

                    is Resource.Error -> {
                        //schedule background retry
                        //backgroundSyncScheduler.scheduleNewsSourcesSync(sourcesEntities)

                        Resource.Success(
                            sources,
                            syncMetaData = DBSyncMetaData(
                                isCached = false,
                                cacheStatus = CacheStatus.SYNCING,
                                cacheError = "Failed to sync ${dbResult.message}.\n Retrying syncing in the background."
                            )
                        )
                    }

                    is Resource.Loading,
                    is Resource.NoChange -> {
                        Resource.Error(
                            ErrorType.UNKNOWN,
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
                    ErrorType.UNKNOWN,
                    "Unexpected error occurred!"
                )
            }
        }
    }

    private suspend fun handleNoChange(): Resource<List<NewsSource>> {
        val sources = local.getNewsSourcesSnapshot()

        return Resource.Success(
            sources.toDomainList(),
            syncMetaData = DBSyncMetaData(
                isCached = true,
                cacheStatus = CacheStatus.SYNCED,
                notNotified = true
            )
        )
    }

    override fun observeNewsSources(): Flow<Resource<List<NewsSource>>> =
        local.getNewsSources()
            .map { entities ->
                Resource.Success(entities.toDomainList()) as Resource<List<NewsSource>>
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