package com.maxi.dailyfeed.domain.repository

import com.maxi.dailyfeed.common.Resource
import com.maxi.dailyfeed.domain.model.NewsSource
import kotlinx.coroutines.flow.Flow

interface NewsSourcesRepository {

    suspend fun fetchAndCacheNewsSources(forceRefresh: Boolean): Resource<List<NewsSource>>

    fun observeNewsSources(): Flow<Resource<List<NewsSource>>>
}