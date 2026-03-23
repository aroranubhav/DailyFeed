package com.maxi.dailyfeed.data.source.local

import com.maxi.dailyfeed.data.source.local.dao.NewsArticlesDao
import com.maxi.dailyfeed.data.source.local.entity.ArticleEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ArticleLocalDataSource @Inject constructor(
    private val dao: NewsArticlesDao
) {

    suspend fun insertNewsArticles(articles: List<ArticleEntity>) =
        dao.insertNewsArticles(articles)

    fun getNewsArticles(): Flow<List<ArticleEntity>> =
        dao.getNewsArticles()

    suspend fun getNewsArticlesSnapshot(): List<ArticleEntity> =
        dao.getNewsArticlesSnapshot()

    suspend fun clearNewsArticles() =
        dao.clearNewsArticles()
}