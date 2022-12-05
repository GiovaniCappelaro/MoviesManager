package com.example.moviesmanager.model.entity

import androidx.annotation.NonNull

data class MovieEntity(
    val id: Int,
    @NonNull
    val name: String,
    @NonNull
    val year: Int,
    val studio: String?,
    val producer: String?,
    @NonNull
    val duration: Long,
    @NonNull
    val watched: Boolean,
    val rating: Int?,
    @NonNull
    val genreId: Int,
)