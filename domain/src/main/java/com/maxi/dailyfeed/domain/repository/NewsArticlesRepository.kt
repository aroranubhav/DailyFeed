package com.maxi.dailyfeed.domain.repository

import com.maxi.dailyfeed.common.Resource
import com.maxi.dailyfeed.domain.model.Article
import kotlinx.coroutines.flow.Flow

interface NewsArticlesRepository {

    suspend fun fetchAndCacheNewsArticles(forceRefresh: Boolean): Resource<List<Article>>

    fun observeNewsArticles(): Flow<Resource<List<Article>>>
}