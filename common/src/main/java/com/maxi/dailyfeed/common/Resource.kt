package com.maxi.dailyfeed.common

sealed class Resource<out T> {

    data class Success<T>(
        val data: T,
        val syncMetaData: DBSyncMetaData? = null
    ) : Resource<T>()

    data class Error(
        val type: ErrorType,
        val message: String?,
        val code: Int? = null
    ) : Resource<Nothing>()

    data object Loading : Resource<Nothing>()

    data object NoChange : Resource<Nothing>()
}