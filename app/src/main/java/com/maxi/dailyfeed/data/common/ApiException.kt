package com.maxi.dailyfeed.data.common

import java.io.IOException

open class ApiException(
    val errorCode: Int,
    val errorMessage: String?,
    val errorBody: String?,
    val requestMethod: String?,
    val requestUrl: String?
) : IOException() {

    override val message: String
        get() = buildString {
            append("HTTP Exception $errorCode")

            if (!errorMessage.isNullOrEmpty()) {
                append(" -- $errorMessage")
            }

            if (!requestMethod.isNullOrEmpty() && !requestUrl.isNullOrEmpty()) {
                append("-- [$requestMethod -- $requestUrl]")
            }

            if (!errorBody.isNullOrEmpty()) {
                append("\n $errorBody")
            }
        }
}