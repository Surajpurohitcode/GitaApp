package com.android.gitaapp.utils

sealed class AppUiState<out T> {
    object Loading : AppUiState<Nothing>()
    data class Success<T>(val data: T) : AppUiState<T>()
    data class Error(val message: String) : AppUiState<Nothing>()
}

