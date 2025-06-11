package com.android.gitaapp.data.remote

import com.android.gitaapp.data.model.TTSRequest
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Streaming

interface ElevenLabsApi {

    @POST("v1/text-to-speech/{voice_id}")
    @Headers("Content-Type: application/json")
    @Streaming
    suspend fun generateSpeech(
        @Header("xi-api-key") apiKey: String,
        @Path("voice_id") voiceId: String,
        @Body request: TTSRequest
    ): Response<ResponseBody>
}