package com.maxi.dailyfeed.data.source.remote

import com.maxi.dailyfeed.data.source.remote.api.NetworkApiService
import com.maxi.dailyfeed.data.source.remote.dto.NewsResponseDto
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val service: NetworkApiService
) {

    suspend fun getNews(
        language: String,
        country: String,
        forceRefresh: Boolean
    ): NewsResponseDto =
        service.getNews(language, country, forceRefresh)

}