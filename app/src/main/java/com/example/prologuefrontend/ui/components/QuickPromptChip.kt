package com.example.prologuefrontend.ui.components

import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun QuickPromptChip(
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    AssistChip(
        onClick = onClick,
        label = { Text(text) },
        modifier = modifier,
        colors = AssistChipDefaults.assistChipColors()
    )
}