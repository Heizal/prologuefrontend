package com.example.prologuefrontend.data.repository

import com.example.prologuefrontend.data.model.UpdateProfileRequest
import com.example.prologuefrontend.data.model.UserResponse
import com.example.prologuefrontend.data.remote.ApiService
import jakarta.inject.Inject
import jakarta.inject.Singleton
import okhttp3.MultipartBody

@Singleton
class UserRepository @Inject constructor(
    private val api: ApiService
) {
    suspend fun getMe(): UserResponse {
        return api.getMe()
    }

    suspend fun updateProfile(username: String?, bio: String?): UserResponse {
        val body = UpdateProfileRequest(username = username, bio = bio)
        return api.updateProfile(body)
    }

    suspend fun uploadAvatar(file: MultipartBody.Part): UserResponse {
        return api.uploadAvatar(file)
    }
}