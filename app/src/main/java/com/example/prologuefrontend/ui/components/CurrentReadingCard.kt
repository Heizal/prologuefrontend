package com.example.prologuefrontend.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
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

@Composable
fun CurrentReadingCard(){
    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1A1A1A)
        ),
        modifier = Modifier.fillMaxWidth()

    ){
        Row(Modifier.padding(16.dp)){
            Image(
                painter = rememberAsyncImagePainter("https://covers.openlibrary.org/b/id/10523357-L.jpg"),
                contentDescription = null,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(Modifier.width(16.dp))
            Column(Modifier.align(Alignment.CenterVertically)){
                Text("Currently Reading", color = Color.White.copy(alpha = 0.7f))
                Text(
                    "Onyx Storm (The Empyrean, #3)",
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
                Text("By Rebecca Yarros", color = Color.White.copy(alpha = 0.7f))
                Spacer(Modifier.height(8.dp))
                LinearProgressIndicator(
                    progress = { 0.8f },
                    modifier = Modifier.fillMaxWidth(0.8f),
                    color = Color.White,
                    trackColor = Color.Gray,
                )
                Text(
                    "80% completed · 20 minutes left",
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 12.sp
                )

            }

        }

    }
}