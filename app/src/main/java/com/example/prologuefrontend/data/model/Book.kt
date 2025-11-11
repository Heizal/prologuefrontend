package com.example.prologuefrontend.data.model

import com.google.gson.annotations.SerializedName

data class Book(
    val id: String,
    val title: String,
    val author: String,
    @SerializedName("thumbnailUrl") val coverUrl: String,
    val progress: Float
)