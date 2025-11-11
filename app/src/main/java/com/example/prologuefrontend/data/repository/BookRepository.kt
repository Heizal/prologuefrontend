package com.example.prologuefrontend.data.repository

import com.example.prologuefrontend.data.model.Book
import com.example.prologuefrontend.data.remote.ApiService
import jakarta.inject.Inject
import jakarta.inject.Singleton

@Singleton
class BookRepository  @Inject constructor(
    private val api: ApiService
) {
    suspend fun getBooks(): List<Book> {
        return api.getBooks()
    }
}