package com.maxi.dailyfeed.data.source.remote.api

import com.maxi.dailyfeed.data.source.common.DataConstants.EndPoints.TOP_HEADLINES
import com.maxi.dailyfeed.data.source.remote.dto.NewsResponseDto
import com.maxi.dailyfeed.data.source.common.DataConstants.QueryParams as QueryParams
import retrofit2.http.GET
import retrofit2.http.Query

interface NetworkApiService {

    @GET(TOP_HEADLINES)
    suspend fun getNews(
        @Query(QueryParams.LANGUAGE) language: String,
        @Query(QueryParams.COUNTRY) country: String
    ): NewsResponseDto
}