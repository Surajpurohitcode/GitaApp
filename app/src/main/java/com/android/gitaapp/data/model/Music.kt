package com.android.gitaapp.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Music(
    val id: String,
    val title: String,
    val artist: String,
    val album: String? = null,
    val duration: Long? = null,
    val thumbnailUrl: String? = null,
    val fileUrl: String
) : Parcelable
