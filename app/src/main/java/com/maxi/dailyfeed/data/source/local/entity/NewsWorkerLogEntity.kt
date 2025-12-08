package com.maxi.dailyfeed.data.source.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.maxi.dailyfeed.data.common.DataConstants.Tables.NEWS_WORKER_LOGS

@Entity(tableName = NEWS_WORKER_LOGS)
data class NewsWorkerLogEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val timeStamp: Long,
    val status: String,
    val message: String?
)
