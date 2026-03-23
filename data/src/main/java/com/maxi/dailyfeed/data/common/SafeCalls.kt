@file:Suppress("UNCHECKED_CAST")

package com.maxi.dailyfeed.data.common

import androidx.sqlite.SQLiteException
import com.maxi.dailyfeed.common.ErrorType
import com.maxi.dailyfeed.common.HttpException
import com.maxi.dailyfeed.common.Resource
import com.maxi.dailyfeed.common.TransportException
import kotlinx.coroutines.CancellationException
import retrofit2.Response
import java.io.IOException

suspend fun <T> safeApiCall(apiCall: suspend () -> Response<T>): Resource<T> {
    return try {
        val response = apiCall()
        val responseCode = response.code()

        if (responseCode == 304) return Resource.NoChange

        if (response.isSuccessful) {
            Resource.Success(response.body() as T)
        } else {
            Resource.Error(
                ErrorType.UNKNOWN,
                response.message(),
                responseCode
            )
        }
    } catch (ce: CancellationException) {
        throw ce
    } catch (e: TransportException.NoConnectivity) {
        Resource.Error(ErrorType.NO_CONNECTIVITY, e.message, null)
    } catch (e: TransportException.Timeout) {
        Resource.Error(ErrorType.TIMEOUT, e.message, null)
    } catch (e: HttpException.Unauthorized) {
        Resource.Error(ErrorType.AUTHORISATION, e.errorMessage, e.errorCode)
    } catch (e: HttpException.Forbidden) {
        Resource.Error(ErrorType.FORBIDDEN, e.errorMessage, e.errorCode)
    } catch (e: HttpException.NotFound) {
        Resource.Error(ErrorType.NOT_FOUND, e.errorMessage, e.errorCode)
    } catch (e: HttpException.ServerError) {
        Resource.Error(ErrorType.SERVER_ERROR, e.errorMessage, e.errorCode)
    } catch (e: HttpException) {
        Resource.Error(ErrorType.UNKNOWN, e.errorMessage, e.errorCode)
    } catch (e: IOException) {
        Resource.Error(ErrorType.UNKNOWN, e.message, null)
    }
}

suspend fun <T> safeDbCall(dbCall: suspend () -> T): Resource<T> {
    return try {
        val response = dbCall()
        Resource.Success(response)
    } catch (ce: CancellationException) {
        throw ce
    } catch (e: SQLiteException) {
        Resource.Error(
            ErrorType.DATABASE,
            e.message
        )
    } catch (e: IllegalStateException) {
        Resource.Error(
            ErrorType.DATABASE,
            e.message
        )
    } catch (e: Exception) {
        Resource.Error(
            ErrorType.UNKNOWN,
            e.message
        )
    }
}