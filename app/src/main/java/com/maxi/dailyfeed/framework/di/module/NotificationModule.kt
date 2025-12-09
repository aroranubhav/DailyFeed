package com.maxi.dailyfeed.framework.di.module

import android.content.Context
import com.maxi.dailyfeed.framework.notification.NotificationsPermissionChecker
import com.maxi.dailyfeed.framework.notification.RefreshNotificationHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NotificationModule {

    @Provides
    @Singleton
    fun provideNotificationPermissionChecker(
        @ApplicationContext context: Context
    ): NotificationsPermissionChecker =
        NotificationsPermissionChecker(context)

    @Provides
    @Singleton
    fun provideRefreshNewsHelper(
        @ApplicationContext context: Context,
        permissionChecker: NotificationsPermissionChecker
    ): RefreshNotificationHelper =
        RefreshNotificationHelper(
            context,
            permissionChecker
        )

}