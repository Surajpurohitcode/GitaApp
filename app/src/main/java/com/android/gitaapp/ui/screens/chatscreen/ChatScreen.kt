package com.android.gitaapp.ui.screens.chatscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn // Added
import androidx.compose.foundation.layout.imePadding // Added
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars // Added
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.size // Added
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions // Added
import androidx.compose.foundation.text.KeyboardOptions // Added
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
// Example for an optional emoji button:
// import androidx.compose.material.icons.outlined.Mood // For an emoji icon
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults // Added
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction // Added
import androidx.compose.ui.text.input.KeyboardCapitalization // Added
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.ai.type.Content
import com.google.firebase.ai.type.asTextOrNull
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.BreakIterator
import java.text.StringCharacterIterator


@Composable
fun ChatScreen(viewModel: ChatViewModel, navController: NavController) {
    val messages by viewModel.messages.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    var userInput by remember { mutableStateOf("") }
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            coroutineScope.launch {
                listState.animateScrollToItem(0)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFEF2D8))
            // Apply system bar (status, navigation) insets to the Column.
            // Then, imePadding will adjust the Column's content when the keyboard appears.
            .padding(WindowInsets.systemBars.asPaddingValues())
            .imePadding()
    ) {

        // Header Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 8.dp), // Consistent padding
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.navigateUp() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.Black
                )
            }
            Text(
                text = "Ask Krishna",
                color = Color.Black,
                textAlign = TextAlign.Center,
                style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                ),
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(48.dp)) // Balance the IconButton for centering text
        }

        // Messages Area
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 8.dp), // Padding for message bubbles
            state = listState,
            reverseLayout = true
        ) {
            itemsIndexed(messages.reversed()) { index, message ->
                val isThisTheLatestMessageInChat = index == 0
                val isAssistantMessage = message.role != "user"
                val animateThisMessage = isThisTheLatestMessageInChat && isAssistantMessage

                ChatBubble(
                    message = message,
                    isLatestAssistantMessage = animateThisMessage
                )
            }
        }

        // Error Message Display
        if (errorMessage != null) {
            Text(
                text = errorMessage ?: "",
                color = Color.Red,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp, horizontal = 16.dp), // Centered padding
                textAlign = TextAlign.Center
            )
        }

        // Input Row - Modernized
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, end = 8.dp, top = 8.dp, bottom = 12.dp), // Padding around the input bar
            verticalAlignment = Alignment.Bottom // Keeps button aligned with bottom of TextField as it grows
        ) {
            // Optional: Emoji Button (Example)
            // IconButton(
            //     onClick = { /* TODO: Emoji picker logic */ },
            //     modifier = Modifier.size(48.dp)
            // ) {
            //     Icon(
            //         Icons.Outlined.Mood, // Example emoji icon
            //         contentDescription = "Emoji",
            //         tint = Color.Gray
            //     )
            // }
            // Spacer(modifier = Modifier.width(8.dp))

            TextField(
                value = userInput,
                onValueChange = { userInput = it },
                modifier = Modifier
                    .weight(1f)
                    .heightIn(min = 48.dp), // Ensure minimum height, TextField can grow
                placeholder = { Text("Message...") },
                shape = RoundedCornerShape(24.dp), // Rounded corners for the TextField
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White, // Clean background
                    unfocusedContainerColor = Color.White,
                    disabledContainerColor = Color(0xFFF0F0F0),
                    cursorColor = Color(0xFF075E54), // WhatsApp-like green cursor
                    focusedIndicatorColor = Color.Transparent, // No underline
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                ),
                textStyle = TextStyle(fontSize = 16.sp),
                keyboardOptions = KeyboardOptions.Default.copy(
                    capitalization = KeyboardCapitalization.Sentences,
                    // Dynamically set IME action: Send if text exists, otherwise Done/Default
                    imeAction = if (userInput.isNotBlank()) ImeAction.Send else ImeAction.Default
                ),
                keyboardActions = KeyboardActions(
                    onSend = {
                        if (userInput.isNotBlank() && !isLoading) {
                            viewModel.sendMessage(userInput)
                            userInput = ""
                        }
                    }
                ),
                singleLine = false, // Allows for multi-line input
                maxLines = 5,       // Limits the TextField growth to 5 lines
            )

            Spacer(modifier = Modifier.width(8.dp))

            val sendButtonActive = userInput.isNotBlank() && !isLoading
            IconButton(
                onClick = {
                    if (sendButtonActive) {
                        viewModel.sendMessage(userInput)
                        userInput = ""
                    }
                },
                enabled = sendButtonActive,
                modifier = Modifier.size(48.dp), // Standard icon button size
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = Color(0xFF075E54), // WhatsApp green for active send button
                    contentColor = Color.White,
                    disabledContainerColor = Color(0xFFB0B0B0), // A more distinct gray for disabled
                    disabledContentColor = Color(0xFFE0E0E0)   // Lighter icon when disabled
                )
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.Send,
                    contentDescription = "Send"
                )
            }
        }
    }
}

@Composable
fun ChatBubble(message: Content, isLatestAssistantMessage: Boolean) {
    val isUser = message.role == "user"
    val text = message.parts.mapNotNull { it.asTextOrNull() }.joinToString(" ").trim()

    if (text.isNotBlank()) { // Only show bubble if there's text
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp),
            horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
        ) {
            Box(
                modifier = Modifier
                    .background(
                        color = if (isUser) Color(0xFFDCF8C6) else Color.White,
                        shape = RoundedCornerShape(
                            topStart = 16.dp,
                            topEnd = 16.dp,
                            bottomEnd = if (isUser) 4.dp else 16.dp,
                            bottomStart = if (isUser) 16.dp else 4.dp
                        )
                    )
                    .padding(horizontal = 12.dp, vertical = 8.dp)
                    .widthIn(min = 0.dp, max = 300.dp) // Max width for bubbles
            ) {
                if (!isUser && isLatestAssistantMessage) {
                    AnimatedText(text)
                } else {
                    Text(text = text, fontSize = 16.sp, color = Color.Black)
                }
            }
        }
    }
}

@Composable
private fun AnimatedText(text: String) {
    val breakIterator = remember(text) { BreakIterator.getCharacterInstance() }
    val typingDelayInMs = 30L
    var substringText by remember(text) { mutableStateOf("") }

    LaunchedEffect(text) {
        substringText = "" // Reset for new text
        breakIterator.text = StringCharacterIterator(text)
        var nextIndex = breakIterator.next()
        while (nextIndex != BreakIterator.DONE) {
            substringText = text.subSequence(0, nextIndex).toString()
            nextIndex = breakIterator.next()
            delay(typingDelayInMs)
        }
    }
    Text(
        text = substringText,
        fontSize = 16.sp,
        color = Color.Black,
    )
}