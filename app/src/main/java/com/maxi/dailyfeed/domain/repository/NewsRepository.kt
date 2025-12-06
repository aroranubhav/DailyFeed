package com.maxi.dailyfeed.domain.repository

import com.maxi.dailyfeed.common.Resource
import com.maxi.dailyfeed.domain.model.Article
import kotlinx.coroutines.flow.Flow

interface NewsRepository {

    fun getNews(
        language: String,
        country: String
    ): Flow<Resource<List<Article>>>

    suspend fun refreshNews(
        language: String,
        country: String,
        forceRefresh: Boolean
    ): Resource<Unit>
}