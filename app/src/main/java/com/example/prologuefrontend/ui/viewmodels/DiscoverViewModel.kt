package com.example.prologuefrontend.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    private val chatHistory = mutableListOf<ChatMessage>()

    fun selectQuickPrompt(text: String) = sendUserMessage(text)

    fun sendUserMessage(text: String) {
        if (text.isBlank()) return
        val userMsg = ChatMessage(text = text.trim(), isUser = true)
        chatHistory += userMsg
        _uiState.value = DiscoverUiState.Chat(chatHistory.toList())

        viewModelScope.launch {
            _uiState.value = DiscoverUiState.Loading
            try {
                val res = repo.fetchRecommendations(text)
                val aiMsg = ChatMessage(text = res.message, isUser = false)
                chatHistory += aiMsg
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

    fun addBook(bookId: String) {
        val current = _uiState.value
        if (current is DiscoverUiState.Recommendations) {
            _uiState.update {
                current.copy(inLibrary = current.inLibrary + bookId)
            }
            viewModelScope.launch {
                try {
                    repo.addBookToLibrary(bookId)
                } catch (_: Throwable) {
                    _uiState.update {
                        current.copy(inLibrary = current.inLibrary - bookId)
                    }
                }
            }
        }
    }

    fun askAgain() {
        val lastUser = chatHistory.lastOrNull { it.isUser }?.text ?: run {
            _uiState.value = DiscoverUiState.Initial
            return
        }
        sendUserMessage(lastUser)
    }

    fun startNewChat() {
        chatHistory.clear() // Clear the previous conversation history
        _uiState.value = DiscoverUiState.Initial // Reset the UI to the initial state
    }

    fun loadConversation(id: String) {
        // For now, we'll treat loading a past conversation as starting a new chat
        // because the logic to fetch and display old messages isn't implemented yet.
        startNewChat()
        // In the future, this would be:
        // viewModelScope.launch {
        //     val messages = repo.getConversation(id)
        //     chatHistory.clear()
        //     chatHistory.addAll(messages)
        //     _uiState.value = DiscoverUiState.Chat(chatHistory.toList())
        // }
    }

}