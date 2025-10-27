package com.example.prologuefrontend.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight

@Composable
fun GreetingSection(username: String) {
    Column {
        Text(
            text = "Good morning $username",
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.Bold
            )
        )
        Text(
            text = "Ready to continue your reading journey",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )
    }
}