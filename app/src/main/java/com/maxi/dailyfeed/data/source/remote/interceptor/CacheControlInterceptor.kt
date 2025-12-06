package com.maxi.dailyfeed.data.source.remote.interceptor

import com.maxi.dailyfeed.data.common.DataConstants.Headers.X_FORCE_REFRESH
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Response
import java.util.concurrent.TimeUnit

class CacheControlInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        val forceRefreshHeader = request.header(X_FORCE_REFRESH) == "true"

        return if (request.method == "GET") {
            val modifiedRequest = when {
                forceRefreshHeader -> {
                    request
                        .newBuilder()
                        .removeHeader(X_FORCE_REFRESH)
                        .cacheControl(CacheControl.FORCE_NETWORK)
                        .build()
                }

                else -> {
                    request
                        .newBuilder()
                        .cacheControl(
                            CacheControl.Builder()
                                .maxAge(2, TimeUnit.MINUTES)
                                .build()
                        )
                        .build()
                }
            }
            chain.proceed(modifiedRequest)
        } else {
            chain.proceed(request)
        }
    }
}