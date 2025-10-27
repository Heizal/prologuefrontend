package com.example.prologuefrontend.data.remote

import com.example.prologuefrontend.data.model.AIPick
import com.example.prologuefrontend.data.model.Book
import retrofit2.http.GET

interface ApiService {
    @GET("/api/recommendations/ai-picks")
    suspend fun getAIPick(): AIPick
    @GET("/books")
    suspend fun getBooks(): List<Book>
}