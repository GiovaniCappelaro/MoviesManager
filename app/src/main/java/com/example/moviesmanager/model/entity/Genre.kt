package com.example.moviesmanager.model.entity

import android.os.Parcelable
import androidx.annotation.NonNull
import kotlinx.parcelize.Parcelize

@Parcelize
data class Genre(
    var id: Int,
    @NonNull
    var name: String,
): Parcelable