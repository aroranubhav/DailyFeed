package com.maxi.dailyfeed.data.source.remote.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import com.maxi.dailyfeed.data.source.common.DataConstants.Headers as Headers

class AuthorizationInterceptor(
    private val apiKey: String,
    private val userAgent: String
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        val modifiedRequest = originalRequest
            .newBuilder()
            .addHeader(Headers.API_KEY, apiKey)
            .addHeader(Headers.USER_AGENT, userAgent)
            .build()

        return chain.proceed(modifiedRequest)
    }
}