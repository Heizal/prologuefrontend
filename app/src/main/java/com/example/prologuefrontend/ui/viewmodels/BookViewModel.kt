package com.example.prologuefrontend.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prologuefrontend.data.model.Book
import com.example.prologuefrontend.data.repository.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class BookState{
    object Loading : BookState()
    data class Success(val books: List<Book>) : BookState()
    data class Error(val message: String) : BookState()

}
@HiltViewModel
class BookViewModel @Inject constructor(
    private val repository: BookRepository
) : ViewModel() {

    private val _state = MutableStateFlow<BookState>(BookState.Loading)
    val state: StateFlow<BookState> = _state

    init {
        observeBooks()
        refresh()
    }

    private fun observeBooks() {
        viewModelScope.launch {
            repository.books.collect { newBooks ->
                _state.value = BookState.Success(newBooks)
            }
        }
    }

    fun refresh() {
        viewModelScope.launch {
            try {
                repository.refreshBooks()
            } catch (e: Exception) {
                _state.value = BookState.Error("Failed to refresh books")
            }
        }
    }
}