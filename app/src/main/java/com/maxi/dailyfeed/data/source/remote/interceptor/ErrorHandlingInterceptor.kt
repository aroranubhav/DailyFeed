package com.maxi.dailyfeed.data.source.remote.interceptor

import com.maxi.dailyfeed.data.common.ApiException
import com.maxi.dailyfeed.data.common.ForbiddenException
import com.maxi.dailyfeed.data.common.InternalServerException
import com.maxi.dailyfeed.data.common.NotFoundException
import com.maxi.dailyfeed.data.common.UnauthorizedException
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

@Serializable
data class ErrorResponse(
    val error: String? = null,
    val message: String? = null
)

class ErrorHandlingInterceptor(
    private val json: Json
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        val response: Response

        try {
            response = chain.proceed(request)
        } catch (e: IOException) {
            throw e
        }

        if (!response.isSuccessful) {
            val errorBody = response.peekBody(Long.MAX_VALUE).toString()

            val parsedError = try {
                json.decodeFromString<ErrorResponse>(errorBody)
            } catch (e: Exception) {
                null
            }

            val errorMessage = parsedError?.error ?: parsedError?.message ?: response.message

            when (response.code) {
                401 -> throw UnauthorizedException(
                    errorMessage,
                    errorBody,
                    request.method,
                    request.url.toString()
                )

                403 -> throw ForbiddenException(
                    errorMessage,
                    errorBody,
                    request.method,
                    request.url.toString()
                )

                404 -> throw NotFoundException(
                    errorMessage,
                    errorBody,
                    request.method,
                    request.url.toString()
                )

                500 -> throw InternalServerException(
                    errorMessage,
                    errorBody,
                    request.method,
                    request.url.toString()
                )

                else -> throw ApiException(
                    response.code,
                    errorMessage,
                    errorBody,
                    request.method,
                    request.url.toString()
                )
            }
        }

        return response
    }
}