package com.android.gitaapp.data.repository

import com.android.gitaapp.data.model.GitaChapter
import com.android.gitaapp.data.model.GitaVerse

interface GitaRepository {
    suspend fun fetchChapters(): Result<List<GitaChapter>>
    suspend fun fetchVerses(chapterNumber: Int, verseNumber: Int): Result<List<GitaVerse>>
}