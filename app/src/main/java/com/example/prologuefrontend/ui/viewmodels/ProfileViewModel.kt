package com.example.prologuefrontend.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prologuefrontend.data.model.ProfileUiState
import com.example.prologuefrontend.data.repository.BookRepository
import com.example.prologuefrontend.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val bookRepository: BookRepository

) : ViewModel() {
    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState

    fun loadProfile() {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true, error = null)

                val user = userRepository.getMe()
                val stats = bookRepository.getProfileStats()

                _uiState.value = ProfileUiState(
                    isLoading = false,
                    username = user.username,
                    bio = user.bio.orEmpty(),
                    profilePictureUrl = user.profilePictureUrl,
                    booksRead = stats.booksRead,
                    currentlyReading = stats.currentlyReading,
                    wantToRead = stats.wantToRead
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Failed to load profile"
                )
            }
        }
    }

    fun updateBio(newBio: String) {
        val current = _uiState.value
        _uiState.value = current.copy(bio = newBio)
    }

    fun saveProfile() {
        val current = _uiState.value
        viewModelScope.launch {
            try {
                val updated = userRepository.updateProfile(
                    username = current.username,
                    bio = current.bio
                )
                _uiState.value = current.copy(
                    username = updated.username,
                    bio = updated.bio.orEmpty(),
                    profilePictureUrl = updated.profilePictureUrl,
                    error = null
                )
            } catch (e: Exception) {
                _uiState.value = current.copy(error = e.message ?: "Failed to save profile")
            }
        }
    }

    fun uploadAvatar(filePart: MultipartBody.Part) {
        val current = _uiState.value
        viewModelScope.launch {
            try {
                val updated = userRepository.uploadAvatar(filePart)
                _uiState.value = current.copy(
                    profilePictureUrl = updated.profilePictureUrl,
                    error = null
                )
            } catch (e: Exception) {
                _uiState.value = current.copy(error = e.message ?: "Failed to upload avatar")
            }
        }
    }


}