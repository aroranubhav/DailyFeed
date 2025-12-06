package com.maxi.dailyfeed.framework.di.module

import android.content.Context
import com.maxi.dailyfeed.data.repository.DefaultNewsRepository
import com.maxi.dailyfeed.data.source.local.LocalDataSource
import com.maxi.dailyfeed.data.source.remote.RemoteDataSource
import com.maxi.dailyfeed.domain.repository.AppDataStore
import com.maxi.dailyfeed.domain.repository.NewsRepository
import com.maxi.dailyfeed.framework.datastore.DefaultAppDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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

    @Provides
    @Singleton
    fun provideAppDataStore(
        @ApplicationContext context: Context
    ): AppDataStore =
        DefaultAppDataStore(context)
}