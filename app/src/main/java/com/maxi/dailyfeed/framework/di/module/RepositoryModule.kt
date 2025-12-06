package com.maxi.dailyfeed.framework.di.module

import com.maxi.dailyfeed.data.repository.DefaultNewsRepository
import com.maxi.dailyfeed.data.source.local.LocalDataSource
import com.maxi.dailyfeed.data.source.remote.RemoteDataSource
import com.maxi.dailyfeed.domain.repository.NewsRepository
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
    fun provideNewsRepository(
        remote: RemoteDataSource,
        local: LocalDataSource
    ): NewsRepository =
        DefaultNewsRepository(remote, local)
}