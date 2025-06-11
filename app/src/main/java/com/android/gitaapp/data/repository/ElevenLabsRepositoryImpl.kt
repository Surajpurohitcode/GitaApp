package com.android.gitaapp.data.repository

import android.content.Context
import android.util.Log
import com.android.gitaapp.data.model.TTSRequest
import com.android.gitaapp.data.remote.ElevenLabsApi
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

class ElevenLabsRepositoryImpl @Inject constructor(
    private val api: ElevenLabsApi,
    @ApplicationContext private val context: Context
) : ElevenLabsRepository {

    private val apiKey = "sk_303ea8be57d6f39e009e9478ae406de8a1c22117ddc76dcb"
    private val voiceId = "zgqefOY5FPQ3bB7OZTVR"

    override suspend fun generateSpeech(text: String): File? {
        return try {
            val response = api.generateSpeech(
                apiKey = apiKey,
                voiceId = voiceId,
                request = TTSRequest(text = text)
            )

            if (response.isSuccessful) {
                val inputStream = response.body()?.byteStream()
                val tempFile = File.createTempFile("speech", ".mp3", context.cacheDir)
                inputStream?.use { input ->
                    FileOutputStream(tempFile).use { output ->
                        input.copyTo(output)
                    }
                }
                tempFile
            } else {
                Log.e("TTSRepo", "API Error: ${response.code()}")
                null
            }
        } catch (e: Exception) {
            Log.e("TTSRepo", "Exception: ${e.localizedMessage}")
            null
        }
    }
}
