package com.maxi.dailyfeed.domain.usecase.refresh_news

import com.maxi.dailyfeed.common.Resource

interface RefreshNewsUseCase {

    suspend fun refreshNews(
        language: String,
        country: String
    ): Resource<Unit>
}