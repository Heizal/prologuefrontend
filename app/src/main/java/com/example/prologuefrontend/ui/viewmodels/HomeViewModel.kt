package com.example.prologuefrontend.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prologuefrontend.data.AuthLocalDataSource
import com.example.prologuefrontend.data.model.Book
import com.example.prologuefrontend.data.model.HomePickUiState
import com.example.prologuefrontend.data.model.HomeUiState
import com.example.prologuefrontend.data.model.RecentActivityUiState
import com.example.prologuefrontend.data.model.RediscoverUiState
import com.example.prologuefrontend.data.remote.ApiService
import com.example.prologuefrontend.data.repository.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val apiService: ApiService,
    private val bookRepository: BookRepository,
    private val authLocalDataSource: AuthLocalDataSource
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState = _uiState

    private val _books = MutableStateFlow<List<Book>>(emptyList())
    val books = _books

    init {
        loadBooks()
        loadHomeData()
    }

    private fun loadBooks() {
        viewModelScope.launch {
            _books.value = bookRepository.getBooks()
        }
    }

    private fun loadHomeData() {
        viewModelScope.launch {

            var userId = authLocalDataSource.getUserId().firstOrNull()

            if (userId == null) {
                kotlinx.coroutines.delay(100)
                userId = authLocalDataSource.getUserId().firstOrNull()
            }

            if (userId == null) {
                Log.e("HomeVM", "❌ userId still NULL after retry — aborting loadHomeData()")
                return@launch
            }

            Log.d("HomeVM", "✅ Loaded userId=$userId for Home API calls")


            // AI Pick
            _uiState.value = _uiState.value.copy(
                aiPick = HomePickUiState(isLoading = true)
            )
            try {
                val ai = apiService.getHomePick(userId)
                _uiState.value = _uiState.value.copy(
                    aiPick = HomePickUiState(
                        title = ai.title,
                        author = ai.author,
                        thumbnailUrl = ai.thumbnailUrl,
                        message = ai.message
                    )
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    aiPick = HomePickUiState(error = "Failed to load AI pick.")
                )
            }

            _uiState.value = _uiState.value.copy(
                rediscover = RediscoverUiState(isLoading = true)
            )
            try {
                val red = apiService.rediscover(userId)
                _uiState.value = _uiState.value.copy(
                    rediscover = RediscoverUiState(
                        title = red.title,
                        author = red.author,
                        thumbnailUrl = red.thumbnailUrl
                    )
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    rediscover = RediscoverUiState(error = "Failed to load rediscover data.")
                )
            }

            _uiState.value = _uiState.value.copy(
                activity = RecentActivityUiState(isLoading = true)
            )
            try {
                val act = apiService.getRecentActivity(userId)
                _uiState.value = _uiState.value.copy(
                    activity = RecentActivityUiState(
                        lastBookTitle = act.latestBook?.title,
                        lastBookAuthor = act.latestBook?.author,
                        lastAIPick = act.latestRecommendation?.title,
                        lastAIPickAuthor = act.latestRecommendation?.author,
                        isLoading = false
                    )
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    activity = RecentActivityUiState(
                        isLoading = false,
                        error = "Failed to load recent activity."
                    )
                )
            }
        }
    }
}
