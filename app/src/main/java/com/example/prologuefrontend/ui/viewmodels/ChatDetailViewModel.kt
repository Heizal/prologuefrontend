package com.example.prologuefrontend.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prologuefrontend.data.model.ChatDetailUiState
import com.example.prologuefrontend.data.model.DetailScreenState
import com.example.prologuefrontend.data.repository.ChatRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatDetailViewModel @Inject constructor(
    private val repo: ChatRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ChatDetailUiState())
    val state: StateFlow<ChatDetailUiState> = _state

    fun loadChatDetail(id: String) {
        _state.update { it.copy(screen = DetailScreenState.Loading) }

        viewModelScope.launch {
            try {
                val detail = repo.getChatDetail(id)
                _state.update { it.copy(screen = DetailScreenState.Loaded(detail)) }
            } catch (e: Exception) {
                _state.update { it.copy(screen = DetailScreenState.Error(e.message ?: "Failed")) }
            }
        }
    }

    fun loadChatPreviews() {
        _state.update { it.copy(sidebar = it.sidebar.copy(isLoading = true, error = null)) }

        viewModelScope.launch {
            try {
                val previews = repo.getChatPreviews()
                _state.update {
                    it.copy(
                        sidebar = it.sidebar.copy(
                            previews = previews,
                            isLoading = false
                        )
                    )
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        sidebar = it.sidebar.copy(
                            isLoading = false,
                            error = e.message
                        )
                    )
                }
            }
        }
    }
}