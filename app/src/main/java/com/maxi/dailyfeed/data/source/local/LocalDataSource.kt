package com.maxi.dailyfeed.data.source.local

import com.maxi.dailyfeed.data.source.local.dao.NewsDao
import com.maxi.dailyfeed.data.source.local.entity.ArticleEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val dao: NewsDao
) {

    suspend fun insertNews(articles: List<ArticleEntity>): List<Long> =
        dao.insertNews(articles)

    fun getNews(): Flow<List<ArticleEntity>> =
        dao.getNews()

    suspend fun clearNews() {
        dao.clearNews()
    }
}