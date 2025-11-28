package com.example.prologuefrontend.data.model

data class DiscoverUiState(
    val sidebar: SidebarState = SidebarState(),
    val screen: ScreenState = ScreenState.Initial
)

data class SidebarState(
    val previews: List<ChatPreview> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

sealed class ScreenState {
    object Initial : ScreenState()
    object Loading : ScreenState()

    data class Recommendations(
        val prompt: String,
        val responseMessage: String,
        val books: List<RecommendationBookDto>,
        val inLibrary: Set<String>
    ) : ScreenState()

    data class Error(val message: String) : ScreenState()

    data class Chat(val messages: List<ChatMessage>) : ScreenState()
}
