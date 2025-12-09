package com.maxi.dailyfeed.presentation.news.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.WorkManager
import com.maxi.dailyfeed.common.DispatcherProvider
import com.maxi.dailyfeed.common.ErrorType
import com.maxi.dailyfeed.common.NetworkConnectivityHelper
import com.maxi.dailyfeed.common.Resource
import com.maxi.dailyfeed.domain.model.Article
import com.maxi.dailyfeed.domain.repository.AppDataStore
import com.maxi.dailyfeed.domain.usecase.get_news.GetNewsUseCase
import com.maxi.dailyfeed.domain.usecase.refresh_news.RefreshNewsUseCase
import com.maxi.dailyfeed.presentation.base.UiEvent
import com.maxi.dailyfeed.presentation.base.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val dataStore: AppDataStore,
    private val getNews: GetNewsUseCase,
    private val refreshNews: RefreshNewsUseCase,
    private val dispatchers: DispatcherProvider,
    private val connectivityHelper: NetworkConnectivityHelper
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<List<Article>>>(UiState.Loading)
    val uiState get() = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<UiEvent>(replay = 0)
    val uiEvent get() = _uiEvent.asSharedFlow()

    init {
        observeNews()
        performInitialRefreshIfNeeded()
    }

    private fun observeNews() {
        getNews.getNews("en", "us")
            .flowOn(dispatchers.io)
            .onEach { result ->
                when (result) {
                    is Resource.Success -> {
                        _uiState.value = UiState.Success(result.data)
                    }

                    is Resource.Loading -> {
                        _uiState.value = UiState.Loading
                    }

                    is Resource.Error -> {
                        emitUiEvent(UiEvent.Error(mapErrorToMessage(result.errorType)))
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    private fun performInitialRefreshIfNeeded() {
        viewModelScope.launch(dispatchers.io) {
            val isAlreadyLaunched = dataStore.getIsAlreadyLaunched()

            if (!isAlreadyLaunched) {
                if (connectivityHelper.hasNetworkConnectivity()) {
                    refreshNews()
                } else {
                    emitUiEvent(UiEvent.Error("No internet connection!"))
                }
            }
        }
    }

    fun refreshNews() {
        viewModelScope.launch(dispatchers.io) {
            emitUiEvent(UiEvent.RefreshStarted)
            val response = refreshNews.refreshNews("en", "us")
            if (response is Resource.Error) {
                emitUiEvent(UiEvent.Error(mapErrorToMessage(response.errorType)))
            } else {
                dataStore.updateIsAlreadyLaunched()
            }
            emitUiEvent(UiEvent.RefreshComplete)
        }
    }

    private suspend fun emitUiEvent(event: UiEvent) {
        withContext(dispatchers.main) {
            _uiEvent.emit(event)
        }
    }

    private fun mapErrorToMessage(errorType: ErrorType): String {
        return when (errorType) {
            ErrorType.IO -> "Network issue occurred. Check your connection."
            ErrorType.API -> "An unexpected API error occurred."
            ErrorType.UNAUTHORISED -> "Authorization failed."
            ErrorType.DATABASE -> "Database error occurred."
            ErrorType.UNKNOWN -> "Something went wrong."
        }
    }

}