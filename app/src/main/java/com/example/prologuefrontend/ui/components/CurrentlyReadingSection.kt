package com.example.prologuefrontend.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.prologuefrontend.ui.viewmodels.BookState
import com.example.prologuefrontend.ui.viewmodels.BookViewModel

@Composable
fun CurrentlyReadingSection(
    viewModel: BookViewModel = viewModel(),
    onBookClick: ((String?) -> Unit)? = null
) {
    val state by viewModel.state.collectAsState()

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        // Section Title
        Text(
            text = "Currently Reading",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold
            ),
            color = Color.Black,
            modifier = Modifier.padding(horizontal = 4.dp)
        )

        Spacer(Modifier.height(12.dp))

        when (state) {
            is BookState.Loading -> {
                Text("Loading...", modifier = Modifier.padding(horizontal = 4.dp))
            }
            is BookState.Error -> {
                Text("Error Loading books", color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(horizontal = 4.dp))
            }
            is BookState.Success -> {
                val books = (state as BookState.Success).books

                if (books.isNotEmpty()) {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(books) { book ->
                            CurrentReadingCard(
                                book = book,
                                onClick = { onBookClick?.invoke(book.infoLink) }
                            )
                        }
                    }
                } else {
                    Text("You aren't reading anything yet.", color = Color.Gray, modifier = Modifier.padding(horizontal = 4.dp))
                }
            }
        }
    }
}