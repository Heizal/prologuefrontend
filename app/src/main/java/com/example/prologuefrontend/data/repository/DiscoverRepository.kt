package com.example.prologuefrontend.data.repository

import com.example.prologuefrontend.data.model.AddBookRequest
import com.example.prologuefrontend.data.model.AddBookResponse
import com.example.prologuefrontend.data.model.RecommendationRequest
import com.example.prologuefrontend.data.model.RecommendationResponse
import com.example.prologuefrontend.data.remote.ApiService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DiscoverRepository @Inject constructor(
    private val api: ApiService
) {
    suspend fun fetchRecommendations(prompt: String): RecommendationResponse{
        return api.getRecommendations(RecommendationRequest(prompt))
    }

    suspend fun addBookToLibrary(bookId: String): AddBookResponse{
        return api.addBook(AddBookRequest(bookId))
    }
}