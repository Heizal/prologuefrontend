package com.example.prologuefrontend.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prologuefrontend.data.model.Book
import com.example.prologuefrontend.data.model.ChatMessage
import com.example.prologuefrontend.data.model.DiscoverUiState
import com.example.prologuefrontend.data.model.RecommendationBookDto
import com.example.prologuefrontend.data.model.ScreenState
import com.example.prologuefrontend.data.repository.ChatRepository
import com.example.prologuefrontend.data.repository.DiscoverRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class DiscoverViewModel @Inject constructor(
    private val repo: DiscoverRepository,
    private val chatRepository: ChatRepository
) : ViewModel() {

    private val _state = MutableStateFlow(DiscoverUiState())
    val state: StateFlow<DiscoverUiState> = _state

    private val chatHistory = mutableListOf<ChatMessage>()
    private var lastPrompt: String? = null

    fun loadChatPreviews() {
        viewModelScope.launch {
            _state.update {
                it.copy(sidebar = it.sidebar.copy(isLoading = true, error = null))
            }

            try {
                val previews = chatRepository.getChatPreviews()
                _state.update {
                    it.copy(sidebar = it.sidebar.copy(previews = previews, isLoading = false, error = null))
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(sidebar = it.sidebar.copy(isLoading = false, error = e.message))
                }
            }
        }
    }

    fun startNewChat() {
        lastPrompt = null
        chatHistory.clear()
        _state.update { it.copy(screen = ScreenState.Initial) }
    }
    fun sendUserMessage(prompt: String) {
        lastPrompt = prompt

        _state.update { it.copy(screen = ScreenState.Loading) }
        chatHistory.add(ChatMessage(text = prompt, isUser = true))

        viewModelScope.launch {
            try {
                val response = repo.fetchRecommendations(prompt)

                chatHistory.add(
                    ChatMessage(
                        text = response.message,
                        isUser = false
                    )
                )

                _state.update {
                    it.copy(
                        screen = ScreenState.Recommendations(
                            prompt = prompt,
                            responseMessage = response.message,
                            books = response.recommendations,
                            inLibrary = emptySet()
                        )
                    )
                }

            } catch (e: Exception) {
                _state.update { it.copy(screen = ScreenState.Error(e.message ?: "Request failed")) }
            }
        }
    }

    fun askAgain() {
        val prompt = lastPrompt ?: return startNewChat()
        sendUserMessage(prompt)
    }

    fun addBook(book: RecommendationBookDto) {
        val screen = _state.value.screen as? ScreenState.Recommendations ?: return

        _state.update {
            it.copy(
                screen = screen.copy(inLibrary = screen.inLibrary + book.id)
            )
        }

        viewModelScope.launch {
            try {
                repo.addBookToLibrary(
                    Book(
                        id = null,
                        title = book.title,
                        author = book.author,
                        thumbnailUrl = book.thumbnailUrl,
                        progress = 0
                    )
                )
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        screen = screen.copy(inLibrary = screen.inLibrary - book.id)
                    )
                }
            }
        }
    }

    fun selectQuickPrompt(text: String) {
        sendUserMessage(text)
    }
}

