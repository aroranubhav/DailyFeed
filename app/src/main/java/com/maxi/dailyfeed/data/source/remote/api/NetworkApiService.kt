package com.maxi.dailyfeed.data.source.remote.api

import com.maxi.dailyfeed.data.source.common.DataConstants.EndPoints.TOP_HEADLINES
import com.maxi.dailyfeed.data.source.common.DataConstants.Headers.X_FORCE_REFRESH
import com.maxi.dailyfeed.data.source.common.DataConstants.QueryParams
import com.maxi.dailyfeed.data.source.remote.dto.NewsResponseDto
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface NetworkApiService {

    @GET(TOP_HEADLINES)
    suspend fun getNews(
        @Query(QueryParams.LANGUAGE) language: String,
        @Query(QueryParams.COUNTRY) country: String,
        @Header(X_FORCE_REFRESH) forceRefresh: Boolean = false
    ): NewsResponseDto
}