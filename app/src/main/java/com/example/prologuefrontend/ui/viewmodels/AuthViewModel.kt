package com.example.prologuefrontend.ui.viewmodels

import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prologuefrontend.data.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

sealed class AuthState{
    object Idle: AuthState()
    object Loading : AuthState()
    object Success : AuthState()
    data class Error(val message: String) : AuthState()
}
class AuthViewModel(
    private val repository: AuthRepository
) : ViewModel() {
    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: MutableStateFlow<AuthState> = _authState

    fun login(email: String, password: String){
        viewModelScope.launch {
            try{
                _authState.value = AuthState.Loading
                repository.login(email, password)
                _authState.value = AuthState.Success
            } catch (e: Exception){
                _authState.value = AuthState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun signup(email: String, password: String) {
        viewModelScope.launch {
            try {
                _authState.value = AuthState.Loading
                repository.signup(email, password)
                _authState.value = AuthState.Success
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun reset() {
        _authState.value = AuthState.Idle
    }

}