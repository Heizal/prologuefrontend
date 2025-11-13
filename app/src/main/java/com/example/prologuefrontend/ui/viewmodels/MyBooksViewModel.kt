package com.example.prologuefrontend.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import android.net.Uri
import com.example.prologuefrontend.data.model.Book
import com.example.prologuefrontend.data.repository.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class MyBooksViewModel @Inject constructor(
    private val bookRepository: BookRepository,
    application: Application
) : AndroidViewModel(application){

    private val _books = MutableStateFlow<List<Book>>(emptyList())
    val books: StateFlow<List<Book>> = _books

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query

    init {
        fetchBooks()
    }

    fun fetchBooks(){
        viewModelScope.launch {
            _books.value = bookRepository.getBooks()
        }
    }

    fun onSearchQueryChange(newQuery: String){
        _query.value = newQuery
        viewModelScope.launch {
            _books.value = bookRepository.getBooks(query = newQuery)

        }
    }

    fun uploadBook(uri: Uri){
        val cacheDir = getApplication<Application>().cacheDir
        viewModelScope.launch {
            bookRepository.uploadBook(uri, cacheDir)
            fetchBooks()
        }
    }

}