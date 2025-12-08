package com.maxi.dailyfeed.data.repository

import com.maxi.dailyfeed.common.Resource
import com.maxi.dailyfeed.data.mapper.toDomainList
import com.maxi.dailyfeed.data.mapper.toEntityList
import com.maxi.dailyfeed.data.common.safeApiCall
import com.maxi.dailyfeed.data.common.safeDbCall
import com.maxi.dailyfeed.data.source.local.LocalDataSource
import com.maxi.dailyfeed.data.source.remote.RemoteDataSource
import com.maxi.dailyfeed.domain.model.Article
import com.maxi.dailyfeed.domain.repository.NewsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DefaultNewsRepository @Inject constructor(
    private val remote: RemoteDataSource,
    private val local: LocalDataSource
) : NewsRepository {

    override fun getNews(
        language: String,
        country: String
    ): Flow<Resource<List<Article>>> = flow {

        emit(Resource.Loading)

        emitAll(
            local.getNews()
                .map { Resource.Success(it.toDomainList()) }
        )
    }

    override suspend fun refreshNews(
        language: String,
        country: String,
        forceRefresh: Boolean
    ): Resource<Unit> {
        return when (val apiResponse = safeApiCall {
            remote.getNews(language, country, forceRefresh)
        }) {
            is Resource.Success -> {
                val articles = apiResponse.data.articles

                val dbResponse = safeDbCall {
                    local.clearNews()
                    local.insertNews(articles.toEntityList())
                }

                when (dbResponse) {
                    is Resource.Success -> {
                        Resource.Success(Unit)
                    }

                    is Resource.Error -> {
                        Resource.Error(
                            dbResponse.errorType,
                            dbResponse.errorMessage
                        )
                    }

                    is Resource.Loading -> {
                        error("Unexpected Loading state inside refreshNews()")
                    }
                }
            }

            is Resource.Error -> {
                Resource.Error(
                    apiResponse.errorType,
                    apiResponse.errorMessage,
                    apiResponse.errorCode
                )
            }

            is Resource.Loading -> {
                error("Unexpected Loading state inside refreshNews()")
            }
        }
    }
}