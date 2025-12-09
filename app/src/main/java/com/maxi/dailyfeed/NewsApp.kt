package com.maxi.dailyfeed

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import androidx.work.WorkManager
import com.maxi.dailyfeed.common.Constants.NEWS_REFRESH_NOTIFICATION_CHANNEL_ID
import com.maxi.dailyfeed.common.Constants.NEWS_REFRESH_NOTIFICATION_NAME
import com.maxi.dailyfeed.framework.work.NewsRefreshScheduler
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class NewsApp : Application(),
    Configuration.Provider {

    @Inject
    lateinit var hiltWorkerFactory: HiltWorkerFactory

    @Inject
    lateinit var workManager: WorkManager

    override val workManagerConfiguration: Configuration
        get() = Configuration
            .Builder()
            .setWorkerFactory(hiltWorkerFactory)
            .build()

    override fun onCreate() {
        super.onCreate()
        NewsRefreshScheduler
            .scheduleWork(
                workManager,
                "en",
                "us"
            )
        createNewsRefreshNotificationChannel()
    }

    private fun createNewsRefreshNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                NEWS_REFRESH_NOTIFICATION_CHANNEL_ID,
                NEWS_REFRESH_NOTIFICATION_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )

            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }
}