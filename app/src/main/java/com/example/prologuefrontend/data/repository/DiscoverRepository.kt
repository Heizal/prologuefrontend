package com.example.prologuefrontend.data.repository

import com.example.prologuefrontend.data.model.Book
import com.example.prologuefrontend.data.model.RecommendationRequest
import com.example.prologuefrontend.data.model.RecommendationResponse
import com.example.prologuefrontend.data.remote.ApiService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DiscoverRepository @Inject constructor(
    private val api: ApiService,
    private val bookRepository: BookRepository
) {

    suspend fun fetchRecommendations(prompt: String): RecommendationResponse{
        return api.getRecommendations(RecommendationRequest(prompt.trim()))
    }

    suspend fun addBookToLibrary(book: Book): Book {
        val saved = api.addBook(book)
        bookRepository.refreshBooks() // 🔥 IMPORTANT
        return saved
    }
}