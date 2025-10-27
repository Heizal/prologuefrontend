package com.example.prologuefrontend.data.model

data class Book(
    val id: String,
    val title: String,
    val author: String,
    val coverUrl: String,
    val progress: Float
)