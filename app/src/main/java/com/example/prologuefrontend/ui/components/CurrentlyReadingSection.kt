package com.example.prologuefrontend.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.prologuefrontend.ui.viewmodels.BookState
import com.example.prologuefrontend.ui.viewmodels.BookViewModel

@Composable
fun CurrentlyReadingSection(viewModel: BookViewModel = viewModel()) {
    val state by viewModel.state.collectAsState()

    Column {
        when (state) {
            is BookState.Loading -> Text("Loading...")
            is BookState.Error   -> Text("Error Loading books")
            is BookState.Success -> {
                val books = (state as BookState.Success).books
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(books) { book ->
                        CurrentReadingCard(book)
                    }

                }
            }
        }
    }
}