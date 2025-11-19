package com.example.prologuefrontend.data.model

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
    val description: String
)

data class RecommendationResponse(
    val message: String,
    val recommendations: List<RecommendationBookDto>
)
