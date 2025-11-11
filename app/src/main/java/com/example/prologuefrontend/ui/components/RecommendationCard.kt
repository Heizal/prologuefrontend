package com.example.prologuefrontend.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.prologuefrontend.data.model.RecommendationBookDto

@Composable
fun RecommendationCard(
    book: RecommendationBookDto,
    isInLibrary: Boolean,
    onAddClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        shape = RoundedCornerShape(16.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .background(
                    Brush.verticalGradient(
                        listOf(
                            MaterialTheme.colorScheme.surfaceVariant,
                            Color(0xFFEAEAEA)
                        )
                    )
                )
                .padding(14.dp)
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                AsyncImage(
                    model = book.thumbnailUrl,
                    contentDescription = "${book.title} cover",
                    modifier = Modifier
                        .size(64.dp)
                        .clip(RoundedCornerShape(8.dp))
                )
                Column(modifier = Modifier.weight(1f)) {
                    Text(book.title, style = MaterialTheme.typography.titleMedium)
                    Text("By ${book.author}", style = MaterialTheme.typography.bodySmall)
                    Spacer(Modifier.height(8.dp))
                    Button(
                        onClick = { if (!isInLibrary) onAddClick(book.id) },
                        enabled = !isInLibrary,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isInLibrary)
                                MaterialTheme.colorScheme.secondaryContainer
                            else
                                MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Text(if (isInLibrary) "In your library" else "+ Add")
                    }
                }
            }
            Spacer(Modifier.height(10.dp))
            if (!book.description.isNullOrBlank()) {
                Text(
                    book.description!!,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}