package com.example.prologuefrontend.ui.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prologuefrontend.data.model.Book
import com.example.prologuefrontend.data.model.ChatMessage
import com.example.prologuefrontend.data.model.ChatPreview
import com.example.prologuefrontend.data.model.RecommendationBookDto
import com.example.prologuefrontend.data.repository.ChatRepository
import com.example.prologuefrontend.data.repository.DiscoverRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed interface DiscoverUiState {
    val chatPreviews: List<ChatPreview>
    val isChatPreviewsLoading: Boolean
    val chatPreviewsError: String?
    data class Initial(
        override val chatPreviews: List<ChatPreview> = emptyList(),
        override val isChatPreviewsLoading: Boolean = false,
        override val chatPreviewsError: String? = null
    ) : DiscoverUiState
    data class Chat(
        val messages: List<ChatMessage>,
        val recommendations: List<RecommendationBookDto> = emptyList(),
        // Overrides
        override val chatPreviews: List<ChatPreview> = emptyList(),
        override val isChatPreviewsLoading: Boolean = false,
        override val chatPreviewsError: String? = null
    ) : DiscoverUiState
    data class Recommendations(
        val assistantMessage: String,
        val books: List<RecommendationBookDto>,
        val inLibrary: Set<String> = emptySet(),
        // Overrides
        override val chatPreviews: List<ChatPreview> = emptyList(),
        override val isChatPreviewsLoading: Boolean = false,
        override val chatPreviewsError: String? = null
    ) : DiscoverUiState
    data class Error(
        val message: String,
        // Overrides
        override val chatPreviews: List<ChatPreview> = emptyList(),
        override val isChatPreviewsLoading: Boolean = false,
        override val chatPreviewsError: String? = null
    ) : DiscoverUiState
    data class Loading(
        // Overrides
        override val chatPreviews: List<ChatPreview> = emptyList(),
        override val isChatPreviewsLoading: Boolean = false,
        override val chatPreviewsError: String? = null
    ) : DiscoverUiState
}


@HiltViewModel
class DiscoverViewModel @Inject constructor(
    private val repo: DiscoverRepository,
    private val chatRepository: ChatRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<DiscoverUiState>(DiscoverUiState.Initial())
    val uiState: StateFlow<DiscoverUiState> = _uiState

    var lastUserPrompt by mutableStateOf<String?>(null)
        private set

    private val chatHistory = mutableListOf<ChatMessage>()

    fun selectQuickPrompt(text: String) = sendUserMessage(text)

    fun sendUserMessage(message: String) {
        lastUserPrompt = message
        val currentPreviews = _uiState.value.chatPreviews
        _uiState.value = DiscoverUiState.Loading(chatPreviews = currentPreviews)

        chatHistory.add(ChatMessage(id = System.currentTimeMillis().toString(), text = message, isUser = true))

        viewModelScope.launch {
            try {
                val res = repo.fetchRecommendations(message)
                chatHistory.add(ChatMessage(id = "ai_${System.currentTimeMillis()}", text = res.message, isUser = false))

                _uiState.value = DiscoverUiState.Recommendations(
                    assistantMessage = res.message,
                    books = res.recommendations,
                    inLibrary = emptySet(),
                    chatPreviews = currentPreviews // Keep the previews
                )
            } catch (t: Throwable) {
                Log.e("DiscoverViewModel", "Error fetching recommendations", t)
                _uiState.value = DiscoverUiState.Error(
                    message = t.message ?: "Request timed out",
                    chatPreviews = currentPreviews // Keep the previews
                )
            }
        }
    }

    fun addBook(book: RecommendationBookDto) {
        val current = _uiState.value
        if (current is DiscoverUiState.Recommendations) {
            _uiState.update {
                current.copy(inLibrary = current.inLibrary + book.id)
            }

            viewModelScope.launch {
                try {
                    Log.d("DiscoverViewModel", "📚 Sending book to backend: ${book.title}")
                    val newBook = Book(
                        id = null,
                        title = book.title,
                        author = book.author,
                        thumbnailUrl = book.thumbnailUrl,
                        progress = 0
                    )

                    repo.addBookToLibrary(newBook)
                    Log.d("DiscoverViewModel", "✅ Added to backend: ${book.title}")

                } catch (e: Throwable) {
                    Log.e("DiscoverViewModel", "❌ Failed to add book", e)
                    _uiState.update {
                        current.copy(inLibrary = current.inLibrary - book.id)
                    }
                }
            }
        }
    }

    fun askAgain() {
        val lastPrompt = lastUserPrompt
        val currentPreviews = _uiState.value.chatPreviews

        if (lastPrompt.isNullOrBlank()) {
            _uiState.value = DiscoverUiState.Initial(chatPreviews = currentPreviews)
            return
        }

        _uiState.value = DiscoverUiState.Loading(chatPreviews = currentPreviews)

        viewModelScope.launch {
            try {
                val res = repo.fetchRecommendations(lastPrompt)
                chatHistory.add(ChatMessage(id = "ai_${System.currentTimeMillis()}", text = res.message, isUser = false))

                _uiState.value = DiscoverUiState.Recommendations(
                    assistantMessage = res.message,
                    books = res.recommendations,
                    inLibrary = emptySet(),
                    chatPreviews = currentPreviews
                )
            } catch (t: Throwable) {
                _uiState.value = DiscoverUiState.Error(
                    message = t.message ?: "Request timed out",
                    chatPreviews = currentPreviews
                )
            }
        }
    }

    fun loadChatPreviews() {
        viewModelScope.launch {
            _uiState.update {
                it.copyCommon(isChatPreviewsLoading = true, chatPreviewsError = null)
            }

            try {
                val previews = chatRepository.getChatPreviews()
                _uiState.update {
                    it.copyCommon(
                        chatPreviews = previews,
                        isChatPreviewsLoading = false
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copyCommon(
                        isChatPreviewsLoading = false,
                        chatPreviewsError = e.message ?: "Failed to load past chats"
                    )
                }
            }
        }
    }

    fun startNewChat() {
        lastUserPrompt = null
        chatHistory.clear()
        val currentPreviews = _uiState.value.chatPreviews
        _uiState.value = DiscoverUiState.Initial(chatPreviews = currentPreviews)
    }

    fun loadConversation(id: String) {
        startNewChat()
    }
    private fun DiscoverUiState.copyCommon(
        chatPreviews: List<ChatPreview> = this.chatPreviews,
        isChatPreviewsLoading: Boolean = this.isChatPreviewsLoading,
        chatPreviewsError: String? = this.chatPreviewsError
    ): DiscoverUiState {
        return when (this) {
            is DiscoverUiState.Initial -> this.copy(
                chatPreviews = chatPreviews,
                isChatPreviewsLoading = isChatPreviewsLoading,
                chatPreviewsError = chatPreviewsError
            )
            is DiscoverUiState.Chat -> this.copy(
                chatPreviews = chatPreviews,
                isChatPreviewsLoading = isChatPreviewsLoading,
                chatPreviewsError = chatPreviewsError
            )
            is DiscoverUiState.Recommendations -> this.copy(
                chatPreviews = chatPreviews,
                isChatPreviewsLoading = isChatPreviewsLoading,
                chatPreviewsError = chatPreviewsError
            )
            is DiscoverUiState.Error -> this.copy(
                chatPreviews = chatPreviews,
                isChatPreviewsLoading = isChatPreviewsLoading,
                chatPreviewsError = chatPreviewsError
            )
            is DiscoverUiState.Loading -> this.copy(
                chatPreviews = chatPreviews,
                isChatPreviewsLoading = isChatPreviewsLoading,
                chatPreviewsError = chatPreviewsError
            )
        }
    }
}

