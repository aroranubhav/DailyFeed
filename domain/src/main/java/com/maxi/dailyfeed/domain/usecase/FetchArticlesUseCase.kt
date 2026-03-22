package com.maxi.dailyfeed.domain.usecase

import com.maxi.dailyfeed.common.Resource
import com.maxi.dailyfeed.domain.model.Article
import com.maxi.dailyfeed.domain.repository.NewsArticlesRepository
import javax.inject.Inject

class FetchArticlesUseCase @Inject constructor(
    private val repository: NewsArticlesRepository
) {

    suspend operator fun invoke(forceRefresh: Boolean): Resource<List<Article>> =
        repository.fetchAndCacheNewsArticles(forceRefresh)
}