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
import com.maxi.dailyfeed.data.source.local.dao.NewsWorkerDao
import com.maxi.dailyfeed.data.source.local.entity.NewsWorkerLogEntity
import com.maxi.dailyfeed.domain.usecase.refresh_news.RefreshNewsUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CancellationException

@HiltWorker
class NewsWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted private val params: WorkerParameters,
    private val refreshNews: RefreshNewsUseCase,
    private val dao: NewsWorkerDao
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return try {
            val language = inputData.getString(LANGUAGE) ?: "en"
            val country = inputData.getString(COUNTRY) ?: "us"
            Log.d(TAG, "doWork: called!")
            when (val response = refreshNews.refreshNews(language, country)) {
                is Resource.Success -> {
                    insertWorkStatusInDb(WorkStatus.SUCCESS)
                    Result.success()
                }

                is Resource.Error -> {
                    logError(response.errorType, response.errorMessage)

                    when (response.errorType) {
                        // Transient/Retriable Errors
                        ErrorType.IO,
                        ErrorType.DATABASE -> {
                            insertWorkStatusInDb(WorkStatus.RETRY)
                            Result.retry()
                        }

                        ErrorType.API -> {
                            when (response.errorCode) {
                                in 500..599, 429 -> { // Transient/Retriable Errors
                                    insertWorkStatusInDb(WorkStatus.RETRY)
                                    Result.retry()
                                }

                                in 400..499 -> { // Non-Transient/Non-Retriable Errors
                                    insertWorkStatusInDb(
                                        WorkStatus.FAILURE,
                                        response.errorMessage
                                    )
                                    Result.failure()
                                }

                                else -> {
                                    insertWorkStatusInDb(
                                        WorkStatus.FAILURE,
                                        response.errorMessage
                                    )
                                    Result.success()
                                }
                            }
                        }

                        ErrorType.UNAUTHORISED,
                        ErrorType.UNKNOWN -> {
                            insertWorkStatusInDb(
                                WorkStatus.FAILURE,
                                response.errorMessage
                            )
                            Result.success()
                        }
                    }
                }

                is Resource.Loading -> {
                    insertWorkStatusInDb(
                        WorkStatus.RETRY,
                        "Invalid loading state!"
                    )
                    Result.failure() //safe api call never returns loading!
                }
            }
        } catch (ce: CancellationException) {
            insertWorkStatusInDb(
                WorkStatus.RETRY,
                ce.message
            )
            Result.failure()
            throw ce
        } catch (t: Throwable) {
            insertWorkStatusInDb(
                WorkStatus.FAILURE,
                t.message
            )
            Result.success()
        }
    }

    private suspend fun insertWorkStatusInDb(
        status: WorkStatus,
        message: String? = null
    ) {
        dao.insertNewsWorkerLog(
            NewsWorkerLogEntity(
                0,
                System.currentTimeMillis(),
                status.name,
                message
            )
        )
    }

    private fun logError(
        type: ErrorType,
        message: String?
    ) {
        Log.e(TAG, "Error while running scheduled task! $type -- $message")
    }
}

enum class WorkStatus {
    SUCCESS,
    FAILURE,
    RETRY
}

private const val TAG = "NewsWorkerTAG"