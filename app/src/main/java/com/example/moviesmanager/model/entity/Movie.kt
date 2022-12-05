package com.example.moviesmanager.model.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Movie(
    var id: Int,
    val name: String,
    val year: Int,
    val studio: String?,
    val producer: String?,
    val duration: Long,
    val watched: Boolean,
    val rating: Int?,
    val genre: Genre,
): Parcelable