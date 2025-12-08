package com.maxi.dailyfeed.framework.work

import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import java.util.concurrent.TimeUnit
import com.maxi.dailyfeed.common.Constants as Const

object NewsRefreshScheduler {

    private const val NEWS_REFRESH_WORK = "news_refresh_work"

    fun scheduleWork(
        workManager: WorkManager,
        language: String,
        country: String
    ) {
        val data = workDataOf(
            Const.LANGUAGE to language,
            Const.COUNTRY to country
        )

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresBatteryNotLow(true)
            .build()

        val request = PeriodicWorkRequestBuilder<NewsWorker>(
            1,
            TimeUnit.HOURS
        )
            .setInputData(data)
            .setConstraints(constraints)
            .setInitialDelay(1, TimeUnit.HOURS)
            .build()

        workManager
            .enqueueUniquePeriodicWork(
                NEWS_REFRESH_WORK,
                ExistingPeriodicWorkPolicy.KEEP,
                request
            )
    }
}