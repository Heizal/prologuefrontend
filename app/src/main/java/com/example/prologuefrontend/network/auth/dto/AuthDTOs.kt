package com.example.prologuefrontend.network.auth.dto

data class SignupRequest(
    val email: String,
     val password: String
)

data class LoginRequest(
    val email: String,
    val password: String
)

data class AuthResponse(
    val token: String,
    val userId: Long
)