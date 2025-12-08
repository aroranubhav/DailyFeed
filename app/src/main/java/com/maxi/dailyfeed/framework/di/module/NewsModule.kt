package com.maxi.dailyfeed.framework.di.module

import com.maxi.dailyfeed.presentation.news.adapter.NewsAdapter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.scopes.ActivityScoped

@Module
@InstallIn(ActivityComponent::class)
object NewsModule {

    @Provides
    @ActivityScoped
    fun provideNewsAdapter():
            NewsAdapter =
        NewsAdapter()

}