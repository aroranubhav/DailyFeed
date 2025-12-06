package com.maxi.dailyfeed.domain.usecase.refresh_news

import com.maxi.dailyfeed.common.Resource
import com.maxi.dailyfeed.domain.repository.NewsRepository
import com.maxi.dailyfeed.domain.usecase.common.Utils
import javax.inject.Inject

class DefaultRefreshNewsUseCase @Inject constructor(
    private val repository: NewsRepository
) : RefreshNewsUseCase {
    override suspend fun refreshNews(
        language: String,
        country: String
    ): Resource<Unit> {
        val (languageCode, countryCode) = Utils.normalizeInputs(language, country)

        return repository.refreshNews(languageCode, countryCode, true)
    }
}