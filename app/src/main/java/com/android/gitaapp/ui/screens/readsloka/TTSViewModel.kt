package com.android.gitaapp.ui.screens.readsloka

import android.media.MediaPlayer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.gitaapp.data.repository.ElevenLabsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TTSViewModel @Inject constructor(
    private val repository: ElevenLabsRepository
) : ViewModel() {

    private val _isPlaying = MutableLiveData<Boolean>()
    val isPlaying: LiveData<Boolean> = _isPlaying

    fun speak(text: String) {
        viewModelScope.launch {
            _isPlaying.value = true

            val audioFile = repository.generateSpeech(text)
            if (audioFile != null) {
                val mediaPlayer = MediaPlayer().apply {
                    setDataSource(audioFile.absolutePath)
                    prepare()
                    start()
                    setOnCompletionListener {
                        _isPlaying.value = false
                    }
                }
            } else {
                _isPlaying.value = false
            }
        }
    }
}
