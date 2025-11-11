package com.example.prologuefrontend.data.remote

import com.example.prologuefrontend.data.model.AIPick
import com.example.prologuefrontend.data.model.AddBookRequest
import com.example.prologuefrontend.data.model.AddBookResponse
import com.example.prologuefrontend.data.model.Book
import com.example.prologuefrontend.data.model.RecommendationRequest
import com.example.prologuefrontend.data.model.RecommendationResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @POST("api/recommendations")
    suspend fun getRecommendations(
        @Body request: RecommendationRequest
    ): RecommendationResponse

    @POST("books/add")
    suspend fun addBook(
        @Body request: AddBookRequest
    ): AddBookResponse

    @GET("/books")
    suspend fun getBooks(): List<Book>
}