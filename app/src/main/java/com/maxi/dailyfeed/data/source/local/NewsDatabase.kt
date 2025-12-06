package com.maxi.dailyfeed.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.maxi.dailyfeed.data.source.local.dao.NewsDao
import com.maxi.dailyfeed.data.source.local.entity.ArticleEntity

@Database(
    entities = [ArticleEntity::class],
    version = 1,
    exportSchema = true
)
abstract class NewsDatabase : RoomDatabase() {

    abstract fun newsDao(): NewsDao
}