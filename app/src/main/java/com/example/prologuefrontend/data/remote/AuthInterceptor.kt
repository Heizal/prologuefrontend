package com.example.prologuefrontend.data.remote

import com.example.prologuefrontend.data.AuthLocalDataSource
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
            var value: String? = null
            authLocalDataSource.getToken().collect { value = it }
            value
        }

        val newReq = if (token != null) {
            request.newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
        } else request

        return chain.proceed(newReq)
    }

}