package com.maxi.dailyfeed.framework.notification

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.maxi.dailyfeed.R
import com.maxi.dailyfeed.presentation.news.ui.NewsActivity
import com.maxi.dailyfeed.common.Constants as Constants
import javax.inject.Inject

class RefreshNotificationHelper @Inject constructor(
    private val context: Context,
    private val permissionChecker: NotificationsPermissionChecker
) {

    companion object {
        private const val NEWS_REFRESH_NOTIFICATION_ID = 1001
    }

    fun showNewsRefreshedNotification() {
        if (!permissionChecker.isPostNotificationPermissionGranted()) {
            return
        }

        val intent = Intent(context, NewsActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent
            .getActivity(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

        val notification = NotificationCompat.Builder(
            context,
            Constants.NEWS_REFRESH_NOTIFICATION_CHANNEL_ID
        )
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(Constants.NEWS_NOTIFICATION_TITLE)
            .setContentText(Constants.NEWS_NOTIFICATION_TEXT)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        NotificationManagerCompat.from(
            context
        ).notify(
            NEWS_REFRESH_NOTIFICATION_ID,
            notification
        )
    }
}