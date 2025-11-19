package com.example.prologuefrontend.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ProfileScreen(
    username: String,
    onLogoutClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {

        OutlinedTextField(
            value = "",
            onValueChange = {},
            placeholder = { Text("Search settings...") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(24.dp))

        // Avatar
        Box(
            modifier = Modifier
                .size(80.dp)
                .background(Color(0xFF6D5DE7), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = username.first().uppercase(),
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White
            )
        }

        Spacer(Modifier.height(12.dp))

        Text(
            text = username,
            style = MaterialTheme.typography.headlineMedium
        )

        Text(
            text = "New Reader 📚",
            color = Color.Gray
        )

        Spacer(Modifier.height(24.dp))

        // Stats row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            ProfileStat("Books Read", "0")
            ProfileStat("Currently Reading", "0")
            ProfileStat("Want to Read", "0")
        }

        Spacer(Modifier.height(32.dp))

        Text(
            text = "Reading Statistics",
            style = MaterialTheme.typography.titleMedium
        )

        Text(
            "View your reading analytics",
            color = Color.Gray
        )

        Spacer(Modifier.height(24.dp))

        Button(
            onClick = { onLogoutClick() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Log out")
        }
    }
}

@Composable
fun ProfileStat(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, style = MaterialTheme.typography.headlineSmall)
        Text(label, color = Color.Gray)
    }
}
