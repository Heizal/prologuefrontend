package com.example.prologuefrontend.data.model

data class UserResponse(
    val id: Long,
    val email: String,
    val username: String,
    val createdAt: String,
    val bio: String? = null,
    val profilePictureUrl: String? = null
)

data class UpdateProfileRequest(
    val username: String?,
    val bio: String?
)

data class ProfileStats(
    val booksRead: Int,
    val currentlyReading: Int,
    val wantToRead: Int
)