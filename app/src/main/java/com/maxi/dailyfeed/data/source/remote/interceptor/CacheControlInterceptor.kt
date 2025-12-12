package com.maxi.dailyfeed.data.source.remote.interceptor

import com.maxi.dailyfeed.data.common.DataConstants.Headers.X_FORCE_REFRESH
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Response
import java.util.concurrent.TimeUnit

class CacheControlInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        if (originalRequest.method != "GET") {
            return chain.proceed(originalRequest)
        }

        val forceRefreshHeader = originalRequest.header(X_FORCE_REFRESH) == "true"

        val modifiedRequest = originalRequest
            .newBuilder()
            .removeHeader(X_FORCE_REFRESH)
            .apply {
                if (forceRefreshHeader) {
                    cacheControl(CacheControl.FORCE_NETWORK)
                } else {
                    cacheControl(
                        CacheControl.Builder()
                            .maxAge(2, TimeUnit.MINUTES)
                            .build()
                    )
                }
            }
            .build()

        val networkResponse = chain.proceed(modifiedRequest)

        return networkResponse
            .newBuilder()
            .removeHeader("Pragma")
            .header(
                "Cache-Control", "public, max-age=120"
            )
            .build()
    }
}