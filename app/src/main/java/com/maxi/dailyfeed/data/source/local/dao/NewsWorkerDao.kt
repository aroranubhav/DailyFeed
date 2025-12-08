package com.maxi.dailyfeed.data.source.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.maxi.dailyfeed.data.source.local.entity.NewsWorkerLogEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NewsWorkerDao {

    @Insert(
        onConflict = OnConflictStrategy.REPLACE
    )
    suspend fun insertNewsWorkerLog(entry: NewsWorkerLogEntity)

    @Query(
        """
            SELECT * FROM news_worker_logs
            ORDER BY timeStamp DESC
        """
    )
    fun getNewsWorkLogs(): Flow<List<NewsWorkerLogEntity>>
}