package com.example.prologuefrontend.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ChatMessageBubble(
    isUser: Boolean,
    message: String,
    modifier: Modifier = Modifier
) {
    val bg = if (isUser) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
    val content = if (isUser) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
    val align = if (isUser) Alignment.CenterEnd else Alignment.CenterStart

    Box(
        modifier = modifier,
        contentAlignment = align
    ) {
        Surface(
            color = bg,
            contentColor = content,
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(
                text = message,
                modifier = Modifier
                    .widthIn(max = 280.dp)
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}