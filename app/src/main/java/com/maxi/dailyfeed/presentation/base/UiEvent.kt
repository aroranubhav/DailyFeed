package com.maxi.dailyfeed.presentation.base

sealed class UiEvent {

    data class Error(val error: String): UiEvent()

    data object RefreshStarted: UiEvent()

    data object RefreshComplete: UiEvent()

    /*
    data object ShowNetworkError : UiEvent()

    data class Navigate(val route: String) : UiEvent()

    data object DismissDialog : UiEvent()
    */
}