package com.example.prologuefrontend.data.repository

import com.example.prologuefrontend.domain.model.Book

class BookRepository {
    fun getBooks(): List<Book> = listOf(
        Book(
            id = "1",
            title = "The Great Gatsby",
            author = "F. Scott Fitzgerald",
            coverUrl = "https://example.com/gatsby.jpg",
            progress = 45
        ),
        Book(
            id = "2",
            title = "1984",
            author = "George Orwell",
            coverUrl = "https://example.com/1984.jpg",
            progress = 80
        ),
        Book(
            id = "3",
            title = "To Kill a Mockingbird",
            author = "Harper Lee",
            coverUrl = "https://example.com/mockingbird.jpg",
            progress = 20
        )
    )
}