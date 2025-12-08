package com.maxi.dailyfeed.data.source.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.maxi.dailyfeed.data.source.local.dao.NewsDao
import com.maxi.dailyfeed.data.source.local.dao.NewsWorkerDao
import com.maxi.dailyfeed.data.source.local.entity.ArticleEntity
import com.maxi.dailyfeed.data.source.local.entity.NewsWorkerLogEntity

@Database(
    entities = [
        ArticleEntity::class,
        NewsWorkerLogEntity::class
    ],
    version = 2,
    exportSchema = true
)
abstract class NewsDatabase : RoomDatabase() {

    abstract fun newsDao(): NewsDao

    abstract fun newsWorkerDao(): NewsWorkerDao
}