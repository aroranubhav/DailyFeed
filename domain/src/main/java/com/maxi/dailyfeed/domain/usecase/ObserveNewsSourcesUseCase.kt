package com.maxi.dailyfeed.domain.usecase

import com.maxi.dailyfeed.common.Resource
import com.maxi.dailyfeed.domain.model.NewsSource
import com.maxi.dailyfeed.domain.repository.NewsSourcesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveNewsSourcesUseCase @Inject constructor(
    private val repository: NewsSourcesRepository
) {

    operator fun invoke(): Flow<Resource<List<NewsSource>>> =
        repository.observeNewsSources()
}