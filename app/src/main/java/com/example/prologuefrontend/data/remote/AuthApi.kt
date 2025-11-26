package com.example.prologuefrontend.data.remote

import com.example.prologuefrontend.network.auth.dto.AuthResponse
import com.example.prologuefrontend.network.auth.dto.LoginRequest
import com.example.prologuefrontend.network.auth.dto.SignupRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("/auth/signup")
    suspend fun signup(@Body req: SignupRequest) : AuthResponse

    @POST("/auth/login")
    suspend fun login(@Body req: LoginRequest): AuthResponse
}