package com.maxi.dailyfeed.data.source.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.maxi.dailyfeed.data.source.local.entity.ArticleEntity
import com.maxi.dailyfeed.data.source.local.entity.NewsWorkerLogEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NewsDao {

    @Insert(
        onConflict = OnConflictStrategy.REPLACE
    )
    suspend fun insertNews(articles: List<ArticleEntity>): List<Long>

    @Query(
        """
        SELECT * FROM articles
        ORDER BY publishedAt IS NULL, publishedAt
    """
    )
    fun getNews(): Flow<List<ArticleEntity>>

    @Query(
        "DELETE FROM articles"
    )
    suspend fun clearNews()

    @Insert(
        onConflict = OnConflictStrategy.REPLACE
    )
    suspend fun insertWorkerEntry(entry: NewsWorkerLogEntity): Long
}