package com.android.gitaapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.android.gitaapp.navigation.AppNavigation
import com.android.gitaapp.ui.screens.chatscreen.ChatViewModel
import com.android.gitaapp.ui.screens.home.GitaViewModel
import com.android.gitaapp.ui.screens.readsloka.TTSViewModel
import com.android.gitaapp.ui.theme.GitaAppTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.InternalSerializationApi

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val gitaViewModel: GitaViewModel by viewModels()
    private val chatViewModel: ChatViewModel by viewModels()
    private val ttsViewModel: TTSViewModel by viewModels()

    @OptIn(InternalSerializationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(
                android.graphics.Color.TRANSPARENT
            )
        )
        setContent {
            val navController = rememberNavController()

            GitaAppTheme {
                AppNavigation(gitaViewModel, chatViewModel, ttsViewModel, navController)
            }
        }
    }
}