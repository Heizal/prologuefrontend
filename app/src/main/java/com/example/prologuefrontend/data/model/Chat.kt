package com.example.prologuefrontend.data.model

import com.google.gson.annotations.SerializedName

data class ChatMessage (
    val id: String = java.util.UUID.randomUUID().toString(),
    val text: String,
    val isUser: Boolean,
    val timestamp: Long = System.currentTimeMillis()
)

data class RecommendationRequest(
    val prompt: String,
)

data class RecommendationBookDto(
    val id: String,
    val title: String,
    val author: String,
    val thumbnailUrl: String,
    //@SerializedName("thumbnailUrl") val coverUrl: String,
    val description: String
)

//WHAT THE BACKEND RETURN
data class RecommendationResponse(
    val message: String,
    val recommendations: List<RecommendationBookDto>
)

data class AddBookRequest(
    val bookId: String
)

data class AddBookResponse(
    val status: String,
    val bookId: String
)
