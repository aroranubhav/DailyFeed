package com.maxi.dailyfeed.framework.work

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.maxi.dailyfeed.common.Constants.COUNTRY
import com.maxi.dailyfeed.common.Constants.LANGUAGE
import com.maxi.dailyfeed.common.ErrorType
import com.maxi.dailyfeed.common.Resource
import com.maxi.dailyfeed.domain.usecase.refresh_news.RefreshNewsUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CancellationException

@HiltWorker
class NewsWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted private val params: WorkerParameters,
    private val refreshNews: RefreshNewsUseCase
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return try {
            val language = inputData.getString(LANGUAGE) ?: "en"
            val country = inputData.getString(COUNTRY) ?: "us"

            when (val response = refreshNews.refreshNews(language, country)) {
                is Resource.Success -> {
                    Result.success()
                }

                is Resource.Error -> {
                    logError(response.errorType, response.errorMessage)

                    when (response.errorType) {
                        // Transient/Retriable Errors
                        ErrorType.IO,
                        ErrorType.DATABASE -> {
                            Result.retry()
                        }

                        ErrorType.API -> {
                            when (response.errorCode) {
                                in 500..599, 429 -> Result.retry() // Transient/Retriable Errors
                                in 400..499 -> Result.failure() // Non-Transient/Non-Retriable Errors
                                else -> Result.failure()
                            }
                        }

                        ErrorType.UNAUTHORISED,
                        ErrorType.UNKNOWN -> {
                            Result.failure()
                        }
                    }
                }

                is Resource.Loading -> {
                    Result.failure() //safe api call never returns loading!
                }
            }
        } catch (ce: CancellationException) {
            Result.failure()
            throw ce
        } catch (t: Throwable) {
            Result.failure()
        }
    }

    private fun logError(
        type: ErrorType,
        message: String?
    ) {
        Log.e(TAG, "Error while running scheduled task! $type -- $message")
    }
}

private const val TAG = "NewsWorkerTAG"