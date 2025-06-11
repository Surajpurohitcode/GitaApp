package com.android.gitaapp.data.repository

import java.io.File

interface ElevenLabsRepository {
    suspend fun generateSpeech(text: String): File?
}
