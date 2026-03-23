package com.maxi.dailyfeed.data.source.remote

import com.maxi.dailyfeed.data.source.remote.api.NetworkApiService
import com.maxi.dailyfeed.data.source.remote.dto.NewsSourcesResponseDto
import retrofit2.Response
import javax.inject.Inject

class NewsSourceRemoteDataSource @Inject constructor(
    private val apiService: NetworkApiService
) {

    suspend fun getNewsSources(forceRefresh: Boolean): Response<NewsSourcesResponseDto> =
        apiService.getNewsSources(
            if (forceRefresh)
                "true" else null
        )
}
