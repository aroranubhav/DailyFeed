package com.maxi.dailyfeed.data.source.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.maxi.dailyfeed.data.source.local.entity.NewsSourceEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NewsSourcesDao {

    @Insert(
        onConflict = OnConflictStrategy.REPLACE
    )
    suspend fun insertNewsSources(sources: List<NewsSourceEntity>)

    @Query(
        """
            SELECT * FROM
                news_sources
            ORDER BY 
                name ASC
        """
    )
    fun getNewsSources(): Flow<List<NewsSourceEntity>>

    @Query(
        """
            SELECT * FROM 
                news_sources
            ORDER BY
                name ASC
        """
    )
    suspend fun getNewsSourcesSnapshot(): List<NewsSourceEntity>

    @Query("DELETE FROM news_sources")
    suspend fun clearNewsSources()
}