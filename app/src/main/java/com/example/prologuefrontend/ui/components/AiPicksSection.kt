import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.prologuefrontend.R
import com.example.prologuefrontend.data.model.HomePickUiState

@Composable
fun AIPicksSection(
    uiState: HomePickUiState,
    modifier: Modifier = Modifier,
    onBookClick: (String?) -> Unit
) {
    Column(modifier = modifier.fillMaxWidth()) {
        // Section Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "AI picks for You",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontFamily = MaterialTheme.typography.titleMedium.fontFamily
                )
            )
            Text(
                text = "More",
                style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray)
            )
        }

        if (uiState.isLoading) {
            Text("Asking the AI for a recommendation...", fontStyle = FontStyle.Italic)
        } else if (uiState.error != null) {
            Text("Could not load recommendation.", color = Color.Red)
        } else {
            AIPickCard(
                title = uiState.title ?: "Unknown Title",
                author = uiState.author ?: "Unknown Author",
                thumbnailUrl = uiState.thumbnailUrl,
                aiMessage = uiState.message ?: "I think you'll enjoy this read!",
                infoLink = uiState.infoLink
            )
        }
    }
}

@Composable
fun AIPickCard(
    title: String,
    author: String,
    thumbnailUrl: String?,
    aiMessage: String,
    infoLink: String?
) {
    val context = LocalContext.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp)
            .clickable {
            if (infoLink != null) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(infoLink))
                context.startActivity(intent)
            } else {
                Toast.makeText(context, "No info available", Toast.LENGTH_SHORT).show()
            }
        },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFFFFFF)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize()
        ) {
            // 1. Book Cover Image
            AsyncImage(
                model = thumbnailUrl,
                contentDescription = "Book cover for $title",
                placeholder = painterResource(id = R.drawable.default_cover),
                error = painterResource(id = R.drawable.default_cover_error),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .width(110.dp)
                    .fillMaxHeight()
                    .background(Color.Gray)
            )

            // 2. Content Column
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Top
            ) {
                // Title
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                // Author
                Text(
                    text = "By $author",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = Color.Gray
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = aiMessage,
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontStyle = FontStyle.Italic,
                        lineHeight = 16.sp
                    ),
                    maxLines = 5,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}