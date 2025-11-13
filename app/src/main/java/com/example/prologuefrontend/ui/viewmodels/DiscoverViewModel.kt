package com.example.prologuefrontend.ui.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prologuefrontend.data.model.Book
import com.example.prologuefrontend.data.model.ChatMessage
import com.example.prologuefrontend.data.model.RecommendationBookDto
import com.example.prologuefrontend.data.repository.DiscoverRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed interface DiscoverUiState {
    data object Initial : DiscoverUiState
    data class Chat(
        val messages: List<ChatMessage>,
        val recommendations: List<RecommendationBookDto> = emptyList()
    ) : DiscoverUiState
    data class Recommendations(
        val assistantMessage: String,
        val books: List<RecommendationBookDto>,
        val inLibrary: Set<String> = emptySet()
    ) : DiscoverUiState

    data class Error(val message: String) : DiscoverUiState
    data object Loading : DiscoverUiState
}

@HiltViewModel
class DiscoverViewModel @Inject constructor(
    private val repo: DiscoverRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<DiscoverUiState>(DiscoverUiState.Initial)
    val uiState: StateFlow<DiscoverUiState> = _uiState

    var lastUserPrompt by mutableStateOf<String?>(null)
        private set

    private val chatHistory = mutableListOf<ChatMessage>()

    fun selectQuickPrompt(text: String) = sendUserMessage(text)

    fun sendUserMessage(message: String) {
        lastUserPrompt = message
        _uiState.value = DiscoverUiState.Loading
        chatHistory.add(ChatMessage(id = System.currentTimeMillis().toString(), text = message, isUser = true))

        viewModelScope.launch {
            _uiState.value = DiscoverUiState.Loading
            try {
                val res = repo.fetchRecommendations(message)
                // record assistant response
                chatHistory.add(ChatMessage(id = "ai_${System.currentTimeMillis()}", text = res.message, isUser = false))
                _uiState.value = DiscoverUiState.Recommendations(
                    assistantMessage = res.message,
                    books = res.recommendations,
                    inLibrary = emptySet()
                )
            } catch (t: Throwable) {
                Log.e("DiscoverViewModel", "Error fetching recommendations", t)
                _uiState.value = DiscoverUiState.Error(t.message ?: "Request timed out")
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
                    val newBook = Book(
                        id = book.id,
                        title = book.title,
                        author = book.author,
                        thumbnailUrl = book.thumbnailUrl,
                        progress = 0f
                    )

                    repo.addBookToLibrary(newBook)

                } catch (_: Throwable) {
                    _uiState.update {
                        current.copy(inLibrary = current.inLibrary - book.id)
                    }
                }
            }
        }
    }

    fun askAgain() {
        val lastPrompt = lastUserPrompt
        if (lastPrompt.isNullOrBlank()) {
            _uiState.value = DiscoverUiState.Initial
            return
        }

        _uiState.value = DiscoverUiState.Loading

        viewModelScope.launch {
            try {
                val res = repo.fetchRecommendations(lastPrompt)
                chatHistory.add(ChatMessage(id = "ai_${System.currentTimeMillis()}", text = res.message, isUser = false))

                _uiState.value = DiscoverUiState.Recommendations(
                    assistantMessage = res.message,
                    books = res.recommendations,
                    inLibrary = emptySet()
                )
            } catch (t: Throwable) {
                _uiState.value = DiscoverUiState.Error(t.message ?: "Request timed out")
            }
        }
    }

    fun startNewChat() {
        lastUserPrompt = null
        chatHistory.clear()
        _uiState.value = DiscoverUiState.Initial
    }

    fun loadConversation(id: String) {
        startNewChat()
    }

}