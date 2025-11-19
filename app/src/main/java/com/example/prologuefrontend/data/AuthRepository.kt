package com.example.prologuefrontend.data

import com.example.prologuefrontend.network.auth.AuthApi
import com.example.prologuefrontend.network.auth.dto.AuthResponse
import com.example.prologuefrontend.network.auth.dto.LoginRequest
import com.example.prologuefrontend.network.auth.dto.SignupRequest
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val localDataSource: AuthLocalDataSource,
    private val api: AuthApi
) {
    suspend fun signup(email: String, password: String, username: String,): AuthResponse{
        val res = api.signup(SignupRequest(email, password, username))
        localDataSource.saveAuth(res.token, res.userId)
        return res
    }

    suspend fun login(email: String, password: String): AuthResponse{
        val res = api.login(LoginRequest(email, password))
        localDataSource.saveAuth(res.token, res.userId)
        return res
    }

    fun getToken() = localDataSource.getToken()

    suspend fun isLoggedIn(): Boolean {
        val token = localDataSource.getToken().first()
        return token != null
    }

    suspend fun logout() = localDataSource.clear()
}