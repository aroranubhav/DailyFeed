package com.maxi.dailyfeed.presentation.base

sealed class UiState<out T> {

    data class Success<T>(val data: T) : UiState<T>()

    data object Loading : UiState<Nothing>()
}