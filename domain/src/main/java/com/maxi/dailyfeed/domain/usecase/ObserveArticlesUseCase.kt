package com.maxi.dailyfeed.domain.usecase

import com.maxi.dailyfeed.common.Resource
import com.maxi.dailyfeed.domain.model.Article
import com.maxi.dailyfeed.domain.repository.NewsArticlesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveArticlesUseCase @Inject constructor(
    private val repository: NewsArticlesRepository
) {

    operator fun invoke(): Flow<Resource<List<Article>>> =
        repository.observeNewsArticles()
}