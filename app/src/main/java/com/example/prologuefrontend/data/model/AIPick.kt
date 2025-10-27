package com.example.prologuefrontend.data.model
data class AIPick(
    val title: String,
    val context: String,
    val book: RecommendedBook
)

data class RecommendedBook(
    val id: Long,
    val title: String,
    val author: String,
    val coverUrl: String,
    val description: String
)