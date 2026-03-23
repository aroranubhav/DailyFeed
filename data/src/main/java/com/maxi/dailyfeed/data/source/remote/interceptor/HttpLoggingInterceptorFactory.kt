package com.maxi.dailyfeed.data.source.remote.interceptor

import okhttp3.logging.HttpLoggingInterceptor

class HttpLoggingInterceptorFactory {

    fun create(isDebug: Boolean): HttpLoggingInterceptor {
        return HttpLoggingInterceptor()
            .apply {
                level = if (isDebug) {
                    HttpLoggingInterceptor.Level.BODY
                } else {
                    HttpLoggingInterceptor.Level.NONE
                }
            }
    }
}