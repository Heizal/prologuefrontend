package com.example.prologuefrontend.data.model

enum class ReadingState {
    CURRENTLY_READING,
    WANT_TO_READ,
    COMPLETED
}

data class Book(
    val id: String,
    val title: String,
    val author: String,
    val thumbnailUrl: String,
    val progress: Float,
    val readingState: ReadingState = ReadingState.WANT_TO_READ
)