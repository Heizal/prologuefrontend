package com.example.prologuefrontend.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.prologuefrontend.ui.viewmodels.HomeViewModel

@Composable
fun RecentActivitySection(homeViewModel: HomeViewModel = hiltViewModel()) {
    val ui by homeViewModel.uiState.collectAsState()
    val state = ui.activity


    Column {
        Text("Recent Activity", fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))

        when {
            state.isLoading -> Text("Loading recent activity...", color = Color.Gray)

            state.error != null -> Text("Unable to load recent activity", color = Color.Red)

            state.lastBookTitle == null && state.lastAIPick == null -> {
                Text("No recent activity yet.", color = Color.Gray)
            }

            else -> {
                Card(
                    Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(Modifier.padding(16.dp)) {

                        state.lastBookTitle?.let {
                            Text("📖 You’re currently reading: $it")
                        }

                        state.lastAIPick?.let {
                            Text("📚 Prologue recommended: $it")
                        }
                    }
                }
            }
        }
    }
}

