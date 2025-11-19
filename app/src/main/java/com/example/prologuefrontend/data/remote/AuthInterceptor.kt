package com.example.prologuefrontend.data.remote

import com.example.prologuefrontend.data.AuthLocalDataSource
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val authLocalDataSource: AuthLocalDataSource
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        if (request.url.encodedPath.startsWith("/auth")) {
            return chain.proceed(request)
        }

        val token = runBlocking {
            authLocalDataSource.getToken().first()
        }

        val newReq = if (token != null) {
            request.newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
        } else request

        val response = chain.proceed(newReq)

        // If token is expired → backend sends 401 or 403 → clear token
        if (response.code == 401 || response.code == 403) {
            runBlocking { authLocalDataSource.clear() }
        }

        return response
    }

}