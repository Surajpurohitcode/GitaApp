package com.android.gitaapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.android.gitaapp.ui.screens.chatscreen.ChatScreen
import com.android.gitaapp.ui.screens.chatscreen.ChatViewModel
import com.android.gitaapp.ui.screens.divineloadingscreen.DivineLoadingScreen
import com.android.gitaapp.ui.screens.home.GitaViewModel
import com.android.gitaapp.ui.screens.home.HomeScreen
import com.android.gitaapp.ui.screens.meditationmusicplayer.MusicPlayer
import com.android.gitaapp.ui.screens.readsloka.ReadSloka
import com.android.gitaapp.ui.screens.readsloka.TTSViewModel

@Composable
fun AppNavigation(
    viewModel: GitaViewModel,
    chatViewModel: ChatViewModel,
    ttsViewModel: TTSViewModel,
    navController: NavHostController
) {
    NavHost(navController = navController, startDestination = Dist.Intro) {
        composable<Dist.Intro> {
            DivineLoadingScreen() {
                navController.navigate(Dist.Home) {
                    popUpTo(Dist.Intro) { inclusive = true }
                }
            }
        }

        composable<Dist.Home> {
            HomeScreen(viewModel, navController)
        }

        composable<Dist.ReadSloka> {
            val readSlokaArgs = it.toRoute<Dist.ReadSloka>()
            ReadSloka(viewModel, ttsViewModel, navController, readSlokaArgs)
        }

        composable<Dist.ChatScreen> {
            ChatScreen(chatViewModel, navController)
        }

        composable<Dist.MusicPlayer> {
            MusicPlayer(navController)
        }

    }
}
