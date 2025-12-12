package com.maxi.dailyfeed.data.source.remote.interceptor

import com.maxi.dailyfeed.domain.repository.AppDataStore
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class ETagInterceptor @Inject constructor(
    private val dataStore: AppDataStore
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        val savedETag = runBlocking {
            dataStore.getETag()
        }

        val updatedRequest = if (savedETag != null) {
            originalRequest
                .newBuilder()
                .header("If-None-Match", savedETag)
                .build()
        } else {
            originalRequest
        }

        val networkResponse = chain.proceed(updatedRequest)

        networkResponse.header("ETag")?.let { eTag ->
            if (savedETag != eTag) {
                runBlocking {
                    dataStore.saveETag(eTag)
                }
            }
        }


        return networkResponse
    }
}
