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
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun RecentActivitySection() {
    Column {
        Text("Recent Activity", fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))
        Card(Modifier.fillMaxWidth(), elevation = CardDefaults.cardElevation(2.dp)) {
            Column(Modifier.padding(16.dp)) {
                Text("📖 You’re currently reading Deep End")
                Text("📚 Prologue has recommended Broken Country")
            }
        }
    }
}
