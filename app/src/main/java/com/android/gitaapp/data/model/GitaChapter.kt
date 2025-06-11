package com.android.gitaapp.data.model

import com.google.gson.annotations.SerializedName

data class GitaChapter(
    @SerializedName("chapter_number")
    val chapterNumber: Int,

    @SerializedName("verses_count")
    val versesCount: Int,

    @SerializedName("name")
    val name: String,

    @SerializedName("translation")
    val translation: String,

    @SerializedName("transliteration")
    val transliteration: String,

    @SerializedName("meaning")
    val meaning: Meaning,

    @SerializedName("summary")
    val summary: Summary
)

data class Meaning(
    @SerializedName("en")
    val english: String,

    @SerializedName("hi")
    val hindi: String
)

data class Summary(
    @SerializedName("en")
    val english: String,

    @SerializedName("hi")
    val hindi: String
)
