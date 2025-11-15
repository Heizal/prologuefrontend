package com.example.prologuefrontend.data

import com.example.prologuefrontend.network.auth.AuthApi
import com.example.prologuefrontend.network.auth.dto.AuthResponse
import com.example.prologuefrontend.network.auth.dto.LoginRequest
import com.example.prologuefrontend.network.auth.dto.SignupRequest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val localDataSource: AuthLocalDataSource,
    private val api: AuthApi
) {
    suspend fun signup(email: String, password: String): AuthResponse{
        val res = api.signup(SignupRequest(email, password))
        localDataSource.saveAuth(res.token, res.userId)
        return res
    }

    suspend fun login(email: String, password: String): AuthResponse{
        val res = api.login(LoginRequest(email, password))
        localDataSource.saveAuth(res.token, res.userId)
        return res
    }

    fun getToken() = localDataSource.getToken()

    fun isLoggedIn(): Boolean {
        return runBlocking { localDataSource.getToken().first() != null }
    }

    suspend fun logout() = localDataSource.clear()
}