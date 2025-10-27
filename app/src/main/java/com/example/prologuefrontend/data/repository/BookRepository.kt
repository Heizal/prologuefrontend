package com.example.prologuefrontend.data.repository

import com.example.prologuefrontend.data.model.Book
import com.example.prologuefrontend.data.remote.RetrofitInstance

class BookRepository {
    suspend fun getBooks(): List<Book> {
        return RetrofitInstance.api.getBooks()
    }
}