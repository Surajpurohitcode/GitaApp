package com.android.gitaapp.data.model

data class TTSRequest(
    val text: String,
    val model_id: String = "eleven_monolingual_v1",
    val voice_settings: VoiceSettings = VoiceSettings()
)

data class VoiceSettings(
    val stability: Float = 0.75f,
    val similarity_boost: Float = 0.75f
)
