package com.example.prologuefrontend.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.rememberAsyncImagePainter

@Composable
fun AIPicksSection() {
    Column {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("AI picks for You", fontWeight = FontWeight.Bold)
            Text("More", color = Color.Gray)
        }
        Spacer(Modifier.height(8.dp))
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = Color.Transparent
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .background(
                        brush = Brush.horizontalGradient(
                            listOf(Color(0xFFD3F9F3), Color(0xFFFDEEE6))
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(16.dp)
            ){
                Column(Modifier.padding(16.dp)) {
                    Text(
                        "Feeling cuddly today?",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        "Based on your 80% reading of Onyx Storm (The Empyrean)",
                        color = Color.DarkGray,
                        fontSize = 12.sp
                    )
                    Spacer(Modifier.height(12.dp))
                    Row {
                        Image(
                            painter = rememberAsyncImagePainter("https://covers.openlibrary.org/b/id/9255426-L.jpg"),
                            contentDescription = null,
                            modifier = Modifier
                                .size(80.dp)
                                .clip(RoundedCornerShape(8.dp)),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(Modifier.width(12.dp))
                        Column(Modifier.align(Alignment.CenterVertically)) {
                            Text("Deep End", fontWeight = FontWeight.Bold)
                            Text("By Ali Hazelwood")
                            Text("Similar fantasy cuddly vibes", fontSize = 12.sp)
                            Spacer(Modifier.height(8.dp))
                            Button(
                                onClick = {},
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF8C8DC))
                            ) { Text("+ Add") }
                        }
                    }
                }

            }

        }
    }
}


