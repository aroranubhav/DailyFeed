package com.maxi.dailyfeed.data.source.remote.api

import com.maxi.dailyfeed.data.common.DataConstants.Headers.X_FORCE_REFRESH
import com.maxi.dailyfeed.data.common.DataConstants.QueryParams.COUNTRY
import com.maxi.dailyfeed.data.common.DataConstants.QueryParams.DEFAULT_COUNTRY
import com.maxi.dailyfeed.data.common.DataConstants.QueryParams.DEFAULT_LANGUAGE
import com.maxi.dailyfeed.data.common.DataConstants.QueryParams.LANGUAGE
import com.maxi.dailyfeed.data.source.remote.dto.NewsArticlesResponseDto
import com.maxi.dailyfeed.data.source.remote.dto.NewsSourcesResponseDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface NetworkApiService {

    @GET("top-headlines/sources")
    suspend fun getNewsSources(
        @Header(X_FORCE_REFRESH) forceRefresh: String? = null
    ): Response<NewsSourcesResponseDto>

    @GET("top-headlines")
    suspend fun getNewsArticles(
        @Header(X_FORCE_REFRESH) forceRefresh: String? = null,
        @Query(LANGUAGE) language: String = DEFAULT_LANGUAGE,
        @Query(COUNTRY) country: String = DEFAULT_COUNTRY
    ): Response<NewsArticlesResponseDto>
}