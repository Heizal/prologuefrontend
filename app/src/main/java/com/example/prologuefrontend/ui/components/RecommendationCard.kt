package com.example.prologuefrontend.ui.components

import android.util.Log
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.ImageLoader
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.example.prologuefrontend.data.model.RecommendationBookDto

@Composable
fun RecommendationCard(
    book: RecommendationBookDto,
    isInLibrary: Boolean,
    onAddClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    ElevatedCard(
        shape = RoundedCornerShape(16.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {
        Column(
            modifier = Modifier
                .background(
                    Brush.verticalGradient(
                        listOf(Color(0xFFF7F7F7), Color(0xFFEDEDED))
                    )
                )
                .padding(16.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.Top
            ) {
                Log.d("ThumbnailCheck", "Thumbnail URL: ${book.thumbnailUrl}")

                // ✅ Force Coil to actually draw with proper scaling
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(book.thumbnailUrl)
                        .crossfade(true)
                        .build(),
                    imageLoader = ImageLoader(context),
                    contentDescription = book.title,
                    modifier = Modifier
                        .size(100.dp)
                        .clip(RoundedCornerShape(10.dp)),
                    contentScale = ContentScale.Crop // ✅ ensures image fits box
                )

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .align(Alignment.CenterVertically)
                ) {
                    Text(
                        text = book.title,
                        fontWeight = FontWeight.SemiBold,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "By ${book.author}",
                        style = MaterialTheme.typography.bodySmall
                    )
                    Spacer(Modifier.height(8.dp))
                    Button(
                        onClick = { if (!isInLibrary) onAddClick(book.id) },
                        enabled = !isInLibrary,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isInLibrary)
                                MaterialTheme.colorScheme.secondaryContainer
                            else
                                Color.Black
                        )
                    ) {
                        Text(
                            text = if (isInLibrary) "In your library" else "+ Add",
                            color = Color.White
                        )
                    }
                }
            }

            Spacer(Modifier.height(10.dp))

            // ✅ Limit description properly
            if (!book.description.isNullOrBlank()) {
                Text(
                    text = book.description
                        .split("\n")
                        .firstOrNull()
                        ?.take(400) // cap long single paragraphs
                        ?.plus("...")
                        ?: "",
                    style = MaterialTheme.typography.bodyMedium,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 5
                )
            }
        }
    }
}
