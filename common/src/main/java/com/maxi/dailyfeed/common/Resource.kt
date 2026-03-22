package com.maxi.dailyfeed.common

sealed class Resource<out T> {

    data class Success<T>(val data: T): Resource<T>()

    data class Error(
        val type: ErrorType,
        val message: String?,
        val code: Int?
    ): Resource<Nothing>()

    data object Loading: Resource<Nothing>()
}