package com.example.prologuefrontend.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.rememberAsyncImagePainter
import com.example.prologuefrontend.data.model.Book

@Composable
fun CurrentReadingCard(book: Book){
    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1A1A1A)
        ),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .width(260.dp) // ✅ fixed width for horizontal slider
            .height(IntrinsicSize.Min)

    ){
        Row(Modifier.padding(16.dp)){
            Image(
                painter = rememberAsyncImagePainter(book.coverUrl),
                contentDescription = book.title,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(Modifier.width(16.dp))

            Column(Modifier.align(Alignment.CenterVertically)){
                Text(
                    book.title,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
                Text("By ${book.author}", color = Color.White.copy(alpha = 0.7f))
                Spacer(Modifier.height(8.dp))
                LinearProgressIndicator(
                    progress = { book.progress/100f },
                    modifier = Modifier.fillMaxWidth(0.8f),
                    color = Color.White,
                    trackColor = Color.Gray,
                )
                Text(
                    "${book.progress}% completed",
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 12.sp
                )

            }

        }

    }
}