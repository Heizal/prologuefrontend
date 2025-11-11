package com.example.prologuefrontend.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.prologuefrontend.ui.viewmodels.DiscoverUiState
import com.example.prologuefrontend.ui.viewmodels.DiscoverViewModel


@Composable
fun AIPicksSection(viewModel: DiscoverViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsState()

    Column {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("AI picks for You", fontWeight = FontWeight.Bold)
            Text("More", color = Color.Gray)
        }
        Spacer(Modifier.height(8.dp))
        when (state){
            is DiscoverUiState.Loading ->{
                Text("Loading...", color = Color.Gray)
            }
            is DiscoverUiState.Error ->{
                Text("Error loading AI picks", color = Color.Red)
            }

            is DiscoverUiState.Chat    -> {
                Text(
                    "Chat in progress... go to Discover to continue your conversation.",
                    color = Color.Gray
                )
            }
            DiscoverUiState.Initial    -> {
                Text(
                    "No AI picks yet. Start a chat on the Discover tab to get recommendations!",
                    color = Color.Gray
                )
            }
            is DiscoverUiState.Recommendations -> {
                val recs = (state as DiscoverUiState.Recommendations).books
                if (recs.isEmpty()) {
                    Text("No recommendations right now.", color = Color.Gray)
                } else {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        recs.take(3).forEach { book ->
                            Text(
                                text = "• ${book.title} by ${book.author}",
                                color = Color.Black
                            )
                        }
                    }
                }
            }
        }
    }
}


