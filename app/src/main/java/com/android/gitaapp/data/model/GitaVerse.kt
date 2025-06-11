package com.android.gitaapp.data.model

data class GitaVerse(
    val id: String,
    val chapter: Int,
    val verse: Int,
    val slok: String,
    val transliteration: String,
    val tej: SwamiTejomayananda,
    val chinmay: SwamiChinmayananda,
)
data class SwamiTejomayananda(
    val ht: String
)
data class SwamiChinmayananda(
    val hc: String
)
