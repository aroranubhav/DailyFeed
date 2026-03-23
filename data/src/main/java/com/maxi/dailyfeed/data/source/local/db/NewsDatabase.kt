package com.maxi.dailyfeed.data.source.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.maxi.dailyfeed.data.source.local.dao.NewsArticlesDao
import com.maxi.dailyfeed.data.source.local.dao.NewsSourcesDao
import com.maxi.dailyfeed.data.source.local.entity.ArticleEntity
import com.maxi.dailyfeed.data.source.local.entity.NewsSourceEntity

@Database(
    entities = [NewsSourceEntity::class,
        ArticleEntity::class],
    version = 1,
    exportSchema = true
)
abstract class NewsDatabase : RoomDatabase() {

    abstract fun newsSourcesDao(): NewsSourcesDao

    abstract fun newsArticlesDao(): NewsArticlesDao
}