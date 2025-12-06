package com.maxi.dailyfeed.framework.di.module

import com.maxi.dailyfeed.domain.repository.NewsRepository
import com.maxi.dailyfeed.domain.usecase.get_news.DefaultGetNewsUseCase
import com.maxi.dailyfeed.domain.usecase.get_news.GetNewsUseCase
import com.maxi.dailyfeed.domain.usecase.refresh_news.DefaultRefreshNewsUseCase
import com.maxi.dailyfeed.domain.usecase.refresh_news.RefreshNewsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    @Singleton
    fun provideGetNewsUseCase(
        repository: NewsRepository
    ): GetNewsUseCase =
        DefaultGetNewsUseCase(repository)

    @Provides
    @Singleton
    fun provideRefreshNewsUseCase(
        repository: NewsRepository
    ): RefreshNewsUseCase =
        DefaultRefreshNewsUseCase(repository)
}