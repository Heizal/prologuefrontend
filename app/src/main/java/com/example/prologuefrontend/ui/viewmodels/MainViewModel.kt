package com.example.prologuefrontend.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.example.prologuefrontend.data.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    val authRepository: AuthRepository
) : ViewModel()