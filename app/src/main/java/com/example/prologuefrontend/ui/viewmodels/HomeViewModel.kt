package com.example.prologuefrontend.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prologuefrontend.data.model.Book
import com.example.prologuefrontend.data.repository.BookRepository
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