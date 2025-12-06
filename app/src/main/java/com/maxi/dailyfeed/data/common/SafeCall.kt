package com.maxi.dailyfeed.data.common

import androidx.sqlite.SQLiteException
import com.maxi.dailyfeed.common.ErrorType
import com.maxi.dailyfeed.common.Resource
import kotlinx.coroutines.CancellationException
import java.io.IOException

suspend fun <T> safeApiCall(apiCall: suspend () -> T): Resource<T> {
    return try {
        val response = apiCall()
        Resource.Success(response)
    } catch (e: CancellationException) {
        throw e
    } catch (e: ApiException) {
        val errorType = when (e.errorCode) {
            401 -> ErrorType.UNAUTHORISED
            else -> ErrorType.API
        }
        Resource.Error(
            errorType,
            e.errorMessage
        )
    } catch (e: IOException) {
        Resource.Error(
            ErrorType.IO,
            e.message
        )
    } catch (e: Exception) {
        Resource.Error(
            ErrorType.UNKNOWN,
            e.message
        )
    }
}

suspend fun <T> safeDbCall(dbCall: suspend () -> T): Resource<T> {
    return try {
        val response = dbCall()
        Resource.Success(response)
    } catch (e: CancellationException) {
        throw e
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
    } catch (e: IOException) {
        Resource.Error(
            ErrorType.IO,
            e.message
        )
    } catch (e: Exception) {
        Resource.Error(
            ErrorType.UNKNOWN,
            e.message
        )
    }
}

