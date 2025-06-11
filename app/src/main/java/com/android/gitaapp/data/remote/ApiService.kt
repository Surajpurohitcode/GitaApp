package com.android.gitaapp.data.remote

import com.android.gitaapp.data.model.GitaChapter
import com.android.gitaapp.data.model.GitaVerse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("chapters")
    suspend fun getGitaChapter(): Response<List<GitaChapter>>

    @GET("slok/{chapter}/{verse}/")
    suspend fun getSloka(
        @Path("chapter") chapter: Int,
        @Path("verse") verse: Int
    ): Response<GitaVerse>

}