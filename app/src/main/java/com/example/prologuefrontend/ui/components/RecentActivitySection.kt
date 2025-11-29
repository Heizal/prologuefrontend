package com.example.prologuefrontend.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.rememberAsyncImagePainter
import com.example.prologuefrontend.R
import com.example.prologuefrontend.ui.viewmodels.HomeViewModel

@Composable
fun RecentActivitySection(
    homeViewModel: HomeViewModel = hiltViewModel(),
    onBookClick: (String?) -> Unit
) {
    val ui by homeViewModel.uiState.collectAsState()
    val state = ui.activity

    if (state.lastBookTitle != null || state.lastAIPick != null) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Recent Activity",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = Color.Black,
                modifier = Modifier.padding(horizontal = 4.dp)
            )

            Spacer(Modifier.height(12.dp))

            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(
                        elevation = 6.dp,
                        shape = RoundedCornerShape(16.dp),
                        ambientColor = Color.LightGray,
                        spotColor = Color.Gray
                    )
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    state.lastBookTitle?.let { title ->
                        ActivityRow(
                            iconModel = R.drawable.currently_reading,
                            textPrefix = "You're currently reading ",
                            highlightText = title,
                            onClick = { onBookClick(state.lastBookInfoLink) }
                        )
                    }

                    state.lastAIPick?.let { title ->
                        ActivityRow(
                            iconModel = R.drawable.recommendation,
                            textPrefix = "Prologue has recommended ",
                            highlightText = title,
                            onClick = { onBookClick(state.lastAIPickInfoLink) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ActivityRow(
    iconModel: Any, // Accepts Res ID (Int) or URL (String)
    textPrefix: String,
    highlightText: String,
    onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        // Icon Container
        Box(
            modifier = Modifier.size(40.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = rememberAsyncImagePainter(model = iconModel),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(32.dp)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        // Text Logic
        Text(
            text = buildAnnotatedString {
                append(textPrefix)
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = Color.Black)) {
                    append(highlightText)
                }
            },
            style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = 15.sp,
                color = Color.Black,
                lineHeight = 20.sp
            )
        )
    }
}

