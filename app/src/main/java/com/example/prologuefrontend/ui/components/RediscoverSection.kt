package com.example.prologuefrontend.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.rememberAsyncImagePainter
import com.example.prologuefrontend.ui.viewmodels.HomeViewModel

@Composable
fun RediscoverSection(
    homeViewModel: HomeViewModel = hiltViewModel(),
    onBookClick: (String?) -> Unit
) {
    val ui by homeViewModel.uiState.collectAsState()
    val state = ui.rediscover

    Column {
        Text("Time to Rediscover", fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))

        when {
            state.isLoading -> {
                Text("Finding books to rediscover...", color = Color.Gray)
            }
            state.error != null -> {
                Text("Unable to load rediscovery suggestion", color = Color.Red)
            }
            state.title == null -> {
                Text("No rediscovery suggestions yet.", color = Color.Gray)
            }
            else -> {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF9E5)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                        onBookClick(state.infoLink)
                    }
                ) {
                    Row(Modifier.padding(16.dp)) {
                        Image(
                            painter = rememberAsyncImagePainter(state.thumbnailUrl),
                            contentDescription = null,
                            modifier = Modifier
                                .size(80.dp)
                                .clip(RoundedCornerShape(8.dp)),
                            contentScale = ContentScale.Crop
                        )

                        Spacer(Modifier.width(12.dp))

                        Column {
                            Text(state.title, fontWeight = FontWeight.Bold)
                            Text("By ${state.author}", color = Color.DarkGray)
                            Spacer(Modifier.height(4.dp))
                            Text("You might want to rediscover this.", fontSize = 12.sp, color = Color.Gray)
                        }
                    }
                }
            }
        }
    }
}

