package com.maxi.dailyfeed.data.source.remote

import com.maxi.dailyfeed.data.source.remote.api.NetworkApiService
import com.maxi.dailyfeed.data.source.remote.dto.NewsArticlesResponseDto
import retrofit2.Response
import javax.inject.Inject

class ArticleRemoteDataSource @Inject constructor(
    private val apiService: NetworkApiService
) {

    suspend fun getNewsArticle(
        forceRefresh: Boolean,
        language: String,
        country: String
    ): Response<NewsArticlesResponseDto> =
        apiService.getNewsArticles(
            forceRefresh = if (forceRefresh)
                "true" else null,
            language = language,
            country = country
        )
}