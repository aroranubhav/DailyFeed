package com.maxi.dailyfeed.data.di.module

import android.content.Context
import androidx.room.Room
import com.maxi.dailyfeed.data.source.local.dao.NewsArticlesDao
import com.maxi.dailyfeed.data.source.local.dao.NewsSourcesDao
import com.maxi.dailyfeed.data.source.local.db.NewsDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    private object Constants {
        const val NEWS_DATABASE = "news_database"
    }

    @Singleton
    @Provides
    fun provideNewsDatabase(
        @ApplicationContext context: Context
    ): NewsDatabase =
        Room.databaseBuilder(
            context,
            NewsDatabase::class.java,
            Constants.NEWS_DATABASE
        ).build()

    @Provides
    @Singleton
    fun provideNewsSourcesDao(
        database: NewsDatabase
    ): NewsSourcesDao =
        database.newsSourcesDao()

    @Provides
    @Singleton
    fun provideNewsArticlesDao(
        database: NewsDatabase
    ): NewsArticlesDao =
        database.newsArticlesDao()
}