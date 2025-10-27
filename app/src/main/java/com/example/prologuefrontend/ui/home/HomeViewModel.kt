package com.example.prologuefrontend.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prologuefrontend.data.repository.BookRepository
import com.example.prologuefrontend.domain.model.Book
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: BookRepository = BookRepository()
) : ViewModel() {
    private val _books = MutableStateFlow<List<Book>>(emptyList())
    val books: MutableStateFlow<List<Book>> = _books

    init{
        loadBooks()
    }

    private fun loadBooks(){
        viewModelScope.launch {
            _books.value = repository.getBooks()
        }
    }
}