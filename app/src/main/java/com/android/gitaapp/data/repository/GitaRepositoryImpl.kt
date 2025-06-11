package com.android.gitaapp.data.repository

import com.android.gitaapp.data.model.GitaChapter
import com.android.gitaapp.data.model.GitaVerse
import com.android.gitaapp.data.remote.ApiService
import javax.inject.Inject

class GitaRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : GitaRepository {

    override suspend fun fetchChapters(): Result<List<GitaChapter>> {
        return try {
            val response = apiService.getGitaChapter() // Assuming this API returns a List<GitaChapter>
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!) // Return the list of GitaChapter
            } else {
                Result.failure(Exception("Failed to fetch chapters"))
            }
        } catch (e: Exception) {
            Result.failure(e) // Catch and return any errors
        }
    }

    override suspend fun fetchVerses(
        chapterNumber: Int,
        verseNumber: Int
    ): Result<List<GitaVerse>> {
        return try {
            val response = apiService.getSloka(chapterNumber, verseNumber)
            if (response.isSuccessful && response.body() != null) {
                Result.success(listOf(response.body()!!)) // Wrap the single object in a list
            } else {
                Result.failure(Exception("API call unsuccessful: ${response.code()} ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


}

