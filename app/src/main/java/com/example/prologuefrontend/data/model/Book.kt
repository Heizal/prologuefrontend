package com.example.prologuefrontend.data.model

enum class ReadingState {
    CURRENTLY_READING,
    WANT_TO_READ,
    COMPLETED
}

data class Book(
    val id: Long? = null,
    val title: String,
    val author: String,
    val thumbnailUrl: String? = null,
    val progress: Int = 0,
    val readingState: ReadingState = ReadingState.WANT_TO_READ,
    val infoLink: String? = null
)