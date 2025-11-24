package com.example.prologuefrontend.data.model

data class ProfileUiState(
    val isLoading: Boolean = true,
    val username: String = "",
    val bio: String = "",
    val profilePictureUrl: String? = null,
    val booksRead: Int = 0,
    val currentlyReading: Int = 0,
    val wantToRead: Int = 0,
    val error: String? = null
)