package com.example.moviesmanager.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Movie(
    val id: Int,
    val name: String,
    val year: Int,
    val studio: String?,
    val producer: String?,
    val duration: Long,
    val watched: Boolean,
    val rating: Int?,
    val genre: Genre,
): Parcelable