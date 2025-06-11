package com.android.gitaapp.ui.screens.home

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.gitaapp.data.model.GitaChapter
import com.android.gitaapp.data.model.GitaVerse
import com.android.gitaapp.data.repository.GitaRepository
import com.android.gitaapp.utils.AppUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class GitaViewModel @Inject constructor(
    private val gitaRepository: GitaRepository
) : ViewModel() {
    private val _chapters =
        mutableStateOf<Result<List<GitaChapter>>>(Result.failure(Exception("No data loaded yet")))
    val chapters: State<Result<List<GitaChapter>>> get() = _chapters

    private val _verses = mutableStateOf<AppUiState<List<GitaVerse>>>(AppUiState.Loading)
    val verses: State<AppUiState<List<GitaVerse>>> = _verses

    fun fetchChapters() {
        viewModelScope.launch {
            _chapters.value = Result.failure(Exception("Loading chapters..."))
            try {
                val result = withContext(Dispatchers.IO) {
                    gitaRepository.fetchChapters()
                }
                if (result.isSuccess) {
                    _chapters.value = Result.success(result.getOrNull() ?: emptyList())
                } else {
                    _chapters.value = Result.failure(Exception("Failed to fetch chapters"))
                }
            } catch (e: Exception) {
                Log.d("Api Error", "Error fetching chapters")
                _chapters.value = Result.failure(Exception("An error occurred: ${e.message}"))
            }
        }
    }

    fun fetchVerses(chapterNumber: Int, verseNumber: Int) {
        viewModelScope.launch {
            _verses.value = AppUiState.Loading
            try {
                val result = withContext(Dispatchers.IO) {
                    gitaRepository.fetchVerses(chapterNumber, verseNumber)
                }
                if (result.isSuccess) {
                    _verses.value = AppUiState.Success(result.getOrNull() ?: emptyList())
                } else {
                    _verses.value = AppUiState.Error("Failed to fetch verses")
                }
            } catch (e: Exception) {
                Log.d("Api Error", "Error fetching verses: ${e.message}")
                _verses.value = AppUiState.Error("An error occurred: ${e.message}")
            }
        }
    }
}