package com.maxi.dailyfeed.common

sealed class Resource<out T> {

    data class Success<T>(val data: T) : Resource<T>()

    data class Error(
        val errorType: ErrorType,
        val errorMessage: String? = null,
        val errorCode: Int? = null
    ) : Resource<Nothing>()

    data object Loading : Resource<Nothing>()
}

enum class ErrorType {

    IO,
    API,
    UNAUTHORISED,
    DATABASE,
    UNKNOWN
}