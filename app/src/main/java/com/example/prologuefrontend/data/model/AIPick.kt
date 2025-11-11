package com.example.prologuefrontend.data.model

import com.google.gson.annotations.SerializedName

data class AIPick(
    val title: String,
    val context: String,
    val book: RecommendedBook
)

data class RecommendedBook(
    val id: Long,
    val title: String,
    val author: String,
    val thumbnailUrl: String,
    //@SerializedName("thumbnailUrl") val coverUrl: String,
    val description: String
)