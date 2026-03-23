package com.maxi.dailyfeed.data.source.remote.interceptor

import com.maxi.dailyfeed.data.common.DataConstants.Headers.USER_AGENT
import com.maxi.dailyfeed.data.common.DataConstants.Headers.X_API_KEY
import okhttp3.Interceptor
import okhttp3.Response

class AuthorizationInterceptor(
    private val apiKey: String,
    private val userAgent: String
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        val updatedRequest = originalRequest
            .newBuilder()
            .header(X_API_KEY, apiKey)
            .header(USER_AGENT, userAgent)
            .build()

        return chain.proceed(updatedRequest)
    }
}