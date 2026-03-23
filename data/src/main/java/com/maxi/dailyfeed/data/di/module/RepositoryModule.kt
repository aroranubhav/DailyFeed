package com.maxi.dailyfeed.data.di.module

import com.maxi.dailyfeed.common.DispatcherProvider
import com.maxi.dailyfeed.data.repository.DefaultArticlesRepository
import com.maxi.dailyfeed.data.repository.DefaultNewsSourcesRepository
import com.maxi.dailyfeed.data.source.local.ArticleLocalDataSource
import com.maxi.dailyfeed.data.source.local.NewsSourceLocalDataSource
import com.maxi.dailyfeed.data.source.remote.ArticleRemoteDataSource
import com.maxi.dailyfeed.data.source.remote.NewsSourceRemoteDataSource
import com.maxi.dailyfeed.domain.repository.ArticlesRepository
import com.maxi.dailyfeed.domain.repository.NewsSourcesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideNewsSourcesRepository(
        local: NewsSourceLocalDataSource,
        remote: NewsSourceRemoteDataSource,
        dispatchers: DispatcherProvider
    ): NewsSourcesRepository =
        DefaultNewsSourcesRepository(
            local,
            remote,
            dispatchers
        )

    @Provides
    @Singleton
    fun provideArticlesRepository(
        local: ArticleLocalDataSource,
        remote: ArticleRemoteDataSource,
        dispatchers: DispatcherProvider
    ): ArticlesRepository =
        DefaultArticlesRepository(
            remote,
            local,
            dispatchers
        )
}