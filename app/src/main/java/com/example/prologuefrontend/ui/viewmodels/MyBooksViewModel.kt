package com.example.prologuefrontend.ui.viewmodels

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import android.net.Uri
import androidx.compose.animation.core.copy
import androidx.lifecycle.ViewModel
import com.example.prologuefrontend.data.model.Book
import com.example.prologuefrontend.data.model.MyBooksUiState
import com.example.prologuefrontend.data.repository.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.concurrent.atomics.update

@HiltViewModel
class MyBooksViewModel @Inject constructor(
    private val repository: BookRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow(MyBooksUiState())
    val uiState: StateFlow<MyBooksUiState> = _uiState.asStateFlow()

    private var allBooksCache: List<Book> = emptyList()

    init {
        loadBooks()
    }

    fun loadBooks() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                // Fetch ALL books once
                val books = repository.getBooks(query = null)
                allBooksCache = books

                // Apply existing query if any
                val currentQuery = _uiState.value.query
                _uiState.update {
                    it.copy(
                        books = filterBooks(books, currentQuery),
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }

    fun onSearchQueryChange(newQuery: String) {
        _uiState.update {
            it.copy(
                query = newQuery,
                books = filterBooks(allBooksCache, newQuery)
            )
        }
    }

    fun uploadBook(uri: Uri) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                repository.uploadBook(uri, context.cacheDir)
                loadBooks()
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = "Upload failed") }
            }
        }
    }

    private fun filterBooks(books: List<Book>, query: String): List<Book> {
        if (query.isBlank()) return books
        return books.filter {
            it.title.contains(query, ignoreCase = true) ||
                    it.author.contains(query, ignoreCase = true)
        }
    }
}