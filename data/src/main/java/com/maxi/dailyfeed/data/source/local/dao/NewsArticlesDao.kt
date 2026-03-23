package com.maxi.dailyfeed.data.source.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.maxi.dailyfeed.data.source.local.entity.ArticleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NewsArticlesDao {

    @Insert(
        onConflict = OnConflictStrategy.REPLACE
    )
    suspend fun insertNewsArticles(articles: List<ArticleEntity>)

    @Query(
        """
            SELECT * FROM
                news_articles
            ORDER BY 
                publishedAt IS NULL DESC, publishedAt DESC 
        """
    )
    fun getNewsArticles(): Flow<List<ArticleEntity>>

    @Query(
        """
            SELECT * FROM
                news_articles
            ORDER BY 
                publishedAt IS NULL DESC, publishedAt DESC 
        """
    )
    suspend fun getNewsArticlesSnapshot(): List<ArticleEntity>

    @Query("DELETE FROM news_articles")
    suspend fun clearNewsArticles()
}