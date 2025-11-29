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
    val description: String,
    val infoLink: String?
)

data class RecommendationResponse(
    val message: String,
    val recommendations: List<RecommendationBookDto>
)

data class ChatPreview(
    val chatId: String,
    val previewTitle: String,
    val timestamp: String,
    val topBookCoverUrl: String?
)

data class ChatDetail(
    val chatId: String,
    val userMessage: String,
    val modelResponse: String,
    val recommendations: List<RecommendationBookDto>
)

data class ChatDetailUiState(
    val screen: DetailScreenState = DetailScreenState.Loading,
    val sidebar: SidebarState = SidebarState()
)

sealed interface DetailScreenState {
    object Loading : DetailScreenState
    data class Error(val message: String) : DetailScreenState
    data class Loaded(val detail: ChatDetail) : DetailScreenState
}
