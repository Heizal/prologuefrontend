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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
fun RediscoverSection() {
    Column {
        Text("Time to Rediscover", fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))
        Card(
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF9E5)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(Modifier.padding(16.dp)) {
                Image(
                    painter = rememberAsyncImagePainter("https://covers.openlibrary.org/b/id/10322870-L.jpg"),
                    contentDescription = null,
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
                Spacer(Modifier.width(12.dp))
                Column(Modifier.align(Alignment.CenterVertically)) {
                    Text("Broken Country", fontWeight = FontWeight.Bold)
                    Text("By Clare Leslie Hall")
                    Text("You read 30% 3 weeks ago", fontSize = 12.sp, color = Color.Gray)
                    Spacer(Modifier.height(4.dp))
                    Button(
                        onClick = {},
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF5CF41))
                    ) { Text("Start reading") }
                }
            }
        }
    }
}

