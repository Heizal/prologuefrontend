package com.example.prologuefrontend.data.model

data class MyBooksUiState(
    val books: List<Book> = emptyList(),
    val query: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)