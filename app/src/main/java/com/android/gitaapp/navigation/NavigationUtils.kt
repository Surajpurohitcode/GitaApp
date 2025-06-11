package com.android.gitaapp.navigation

import kotlinx.serialization.Serializable

sealed class Dist {
    @Serializable
    data object Intro : Dist()

    @Serializable
    data object Home : Dist()
    @Serializable
    data class ReadSloka(val chapterNumber: Int) : Dist()
    @Serializable
    data object ChatScreen : Dist()
    @Serializable
    data object MusicPlayer : Dist()
}