package com.maxi.dailyfeed.data.source.local

import com.maxi.dailyfeed.data.source.local.dao.NewsSourcesDao
import com.maxi.dailyfeed.data.source.local.entity.NewsSourceEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class NewsSourceLocalDataSource @Inject constructor(
    private val dao: NewsSourcesDao,
) {

    suspend fun insertNewsSources(sources: List<NewsSourceEntity>) =
        dao.insertNewsSources(sources)

    fun getNewsSources(): Flow<List<NewsSourceEntity>> =
        dao.getNewsSources()

    suspend fun getNewsSourcesSnapshot(): List<NewsSourceEntity> =
        dao.getNewsSourcesSnapshot()

    suspend fun clearNewsSources() =
        dao.clearNewsSources()
}