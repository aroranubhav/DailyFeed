package com.maxi.dailyfeed.domain.usecase

import com.maxi.dailyfeed.common.Resource
import com.maxi.dailyfeed.domain.model.NewsSource
import com.maxi.dailyfeed.domain.repository.NewsSourcesRepository
import javax.inject.Inject

class FetchNewsSourcesUseCase @Inject constructor(
    private val repository: NewsSourcesRepository
) {

    suspend operator fun invoke(forceRefresh: Boolean): Resource<List<NewsSource>> =
        repository.fetchAndCacheNewsSources(forceRefresh)
}