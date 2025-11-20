package com.example.prologuefrontend.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prologuefrontend.data.model.ChatDetail
import com.example.prologuefrontend.data.model.ChatPreview
import com.example.prologuefrontend.data.repository.ChatRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ChatDetailUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val chatDetail: ChatDetail? = null,

    // Sidebar specific state
    val chatPreviews: List<ChatPreview> = emptyList(),
    val isPreviewsLoading: Boolean = false,
    val previewsError: String? = null
)

@HiltViewModel
class ChatDetailViewModel @Inject constructor(
    private val repo: ChatRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChatDetailUiState())
    val uiState: StateFlow<ChatDetailUiState> = _uiState

    fun loadChatDetail(chatId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val detail = repo.getChatDetail(chatId)
                _uiState.update { it.copy(isLoading = false, chatDetail = detail) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.message ?: "Failed to load chat") }
            }
        }
    }

    fun loadChatPreviews() {
        viewModelScope.launch {
            // Don't wipe existing previews if we already have them, just update loading status
            _uiState.update { it.copy(isPreviewsLoading = true, previewsError = null) }

            try {
                val previews = repo.getChatPreviews()
                _uiState.update {
                    it.copy(
                        isPreviewsLoading = false,
                        chatPreviews = previews
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isPreviewsLoading = false,
                        previewsError = e.message ?: "Failed to load history"
                    )
                }
            }
        }
    }
}