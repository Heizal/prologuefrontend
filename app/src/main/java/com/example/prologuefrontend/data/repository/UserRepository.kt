package com.example.prologuefrontend.data.repository

import com.example.prologuefrontend.data.model.UserResponse
import com.example.prologuefrontend.data.remote.ApiService
import jakarta.inject.Inject
import jakarta.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val api: ApiService
) {
    suspend fun getMe(): UserResponse {
        return api.getMe()
    }
}