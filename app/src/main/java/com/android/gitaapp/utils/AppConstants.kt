package com.android.gitaapp.utils

import com.android.gitaapp.data.model.Music

object AppConstants {
    val demoMusicList = listOf(
        Music(
            id = "1",
            title = "Healing Breeze",
            artist = "Ananda Sounds",
            album = "Nature Therapy",
            duration = 180000L,
            thumbnailUrl = "https://example.com/thumb1.jpg",
            fileUrl = "https://example.com/music1.mp3"
        ),
        Music(
            id = "2",
            title = "Deep Relaxation",
            artist = "Inner Peace",
            album = "Meditation Vibes",
            duration = 240000L,
            thumbnailUrl = "https://example.com/thumb2.jpg",
            fileUrl = "https://example.com/music2.mp3"
        ),
        Music(
            id = "3",
            title = "Morning Om Chant",
            artist = "Divine Flow",
            album = "Sacred Chants",
            duration = 210000L,
            thumbnailUrl = "https://example.com/thumb3.jpg",
            fileUrl = "https://example.com/music3.mp3"
        ),
        Music(
            id = "4",
            title = "Chakra Alignment",
            artist = "Zen Garden",
            album = "Healing Frequencies",
            duration = 300000L,
            thumbnailUrl = "https://example.com/thumb4.jpg",
            fileUrl = "https://example.com/music4.mp3"
        ),
        Music(
            id = "5",
            title = "Silent Forest",
            artist = "Nature Echo",
            album = "Tranquil Nature",
            duration = 195000L,
            thumbnailUrl = "https://example.com/thumb5.jpg",
            fileUrl = "https://example.com/music5.mp3"
        )
    )
}