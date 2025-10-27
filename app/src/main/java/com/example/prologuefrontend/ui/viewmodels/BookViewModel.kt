package com.example.prologuefrontend.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prologuefrontend.data.model.Book
import com.example.prologuefrontend.data.repository.BookRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class BookState{
    object Loading : BookState()
    data class Success(val books: List<Book>) : BookState()
    data class Error(val message: String) : BookState()

}
class BookViewModel(
    private val repository: BookRepository = BookRepository()
) : ViewModel() {
    private val _state = MutableStateFlow<BookState>(BookState.Loading)
    val state: StateFlow<BookState> = _state

    init {
        fetchBooks()
    }

    private fun fetchBooks(){
        viewModelScope.launch{
            try{
                val response = repository.getBooks()
                _state.value = BookState.Success(response)
            } catch (e: Exception){
                _state.value = BookState.Error("Failed to fetch books")
            }

        }
    }





}