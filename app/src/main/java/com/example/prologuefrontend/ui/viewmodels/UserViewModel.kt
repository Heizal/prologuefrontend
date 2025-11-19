package com.example.prologuefrontend.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prologuefrontend.data.model.UserResponse
import com.example.prologuefrontend.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class UserViewModel @Inject constructor(
    private val repository: UserRepository
): ViewModel() {
    private val _user = MutableStateFlow<UserResponse?>(null)
    val user = _user

    fun loadUser(){
        viewModelScope.launch {
            try{
                _user.value = repository.getMe()

            } catch (e: Exception){
                e.printStackTrace()

            }
        }
    }
    fun clear() {
        _user.value = null
    }
}