package com.android.gitaapp.ui.screens.chatscreen

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.ai.Chat
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.Content
import com.google.firebase.ai.type.GenerativeBackend
import com.google.firebase.ai.type.TextPart
import com.google.firebase.ai.type.asTextOrNull
import com.google.firebase.ai.type.content
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class ChatViewModel @Inject constructor() : ViewModel() {
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _messageList = mutableStateListOf<Content>()
    private val _messages = MutableStateFlow<List<Content>>(_messageList)
    val messages: StateFlow<List<Content>> = _messages

    private var contentBuilder = Content.Builder()
    private var chat: Chat

    private val prompt = content { text("What else is important when traveling?") }

    val initialPrompt: String =
        prompt?.parts
            ?.filterIsInstance<TextPart>()
            ?.first()
            ?.asTextOrNull().orEmpty()


    init {
        val krishnaSystemPrompt = content {
            text(
                "You are Lord Krishna from the Bhagavad Gita. Speak with wisdom, serenity, and clarity. " + "Use ancient Indian philosophy, guide with dharma, and respond like a divine friend. Use calm, metaphorical speech."
            )
        }

        val generativeModel = Firebase.ai(
            backend = GenerativeBackend.googleAI()
        ).generativeModel(
            modelName = "gemini-2.0-flash", systemInstruction = krishnaSystemPrompt
        )

        chat = generativeModel.startChat()

        // Initial greeting from Krishna
        val initialGreeting = content(role = "model") { text("Great to meet you. What would you like to know?") }

        _messageList.add(initialGreeting)
        _messages.value = _messageList
    }

    fun sendMessage(userMessage: String) {
        val prompt = contentBuilder
            .text(userMessage)
            .build()

        _messageList.add(prompt)
        _messages.value = _messageList

        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = chat.sendMessage(prompt)
                val botReply = response.candidates.first().content
                _messageList.add(botReply)
                _messages.value = _messageList
                _errorMessage.value = null
            } catch (e: Exception) {
                _errorMessage.value = e.localizedMessage
            } finally {
                _isLoading.value = false
                contentBuilder = Content.Builder() // reset for next message
            }
        }
    }
}