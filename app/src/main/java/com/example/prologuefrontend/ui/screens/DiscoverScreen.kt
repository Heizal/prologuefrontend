package com.example.prologuefrontend.ui.screens

import android.annotation.SuppressLint
import android.graphics.drawable.BitmapDrawable
import android.view.Surface
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.palette.graphics.Palette
import coil3.ImageLoader
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.allowHardware
import com.example.prologuefrontend.R
import com.example.prologuefrontend.data.model.ChatMessage
import com.example.prologuefrontend.data.model.RecommendationBookDto
import com.example.prologuefrontend.ui.components.ChatMessageBubble
import com.example.prologuefrontend.ui.components.QuickPromptChip
import com.example.prologuefrontend.ui.components.RecommendationCard
import com.example.prologuefrontend.ui.viewmodels.DiscoverUiState
import com.example.prologuefrontend.ui.viewmodels.DiscoverViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiscoverScreen(vm: DiscoverViewModel = hiltViewModel()) {
    val state by vm.uiState.collectAsState()
    var input by remember { mutableStateOf("") }
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            PastChatsDrawer(
                onNew = { vm.startNewChat() },
                onSelect = { id -> vm.loadConversation(id); scope.launch { drawerState.close() } }
            )
        }
    ) {
        Box(Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 72.dp) // make space for chat input above bottom bar
            ) {
                DiscoverTopBar(
                    onMenuClick = { scope.launch { drawerState.open() } },
                    onNewChatClick = { vm.startNewChat() }
                )

                Box(Modifier.weight(1f).fillMaxWidth()) {
                    when (state) {
                        DiscoverUiState.Initial -> InitialHero(onChip = vm::selectQuickPrompt)
                        is DiscoverUiState.Chat -> ChatView((state as DiscoverUiState.Chat).messages)
                        is DiscoverUiState.Recommendations -> {
                            val rec = state as DiscoverUiState.Recommendations
                            RecommendationsView(
                                assistantMessage = rec.assistantMessage,
                                books = rec.books,
                                inLibrary = rec.inLibrary,
                                onAskAgain = vm::askAgain,
                                onAdd = vm::addBook
                            )
                        }
                        DiscoverUiState.Loading -> CenterLoading()
                        is DiscoverUiState.Error -> ErrorView((state as DiscoverUiState.Error).message)
                    }
                }
            }

            // Chat bar above nav
            DiscoverInputBar(
                value = input,
                onValueChange = { input = it },
                onSend = {
                    if (input.isNotBlank()) {
                        vm.sendUserMessage(input)
                        input = ""
                    }
                },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .navigationBarsPadding()
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DiscoverTopBar(
    onMenuClick: () -> Unit,
    onNewChatClick: () -> Unit
) {
    TopAppBar(
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Prologue ", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleLarge)
                Text(".", color = Color(0xFF458445), style = MaterialTheme.typography.titleLarge)
            }
        },
        actions = {
            IconButton(onClick = { }) {
                Icon(Icons.Outlined.Notifications, contentDescription = "Notifications")
            }
            IconButton(onClick = onNewChatClick) {
                Icon(Icons.Outlined.Edit, contentDescription = "New Chat")
            }
        }
    )
}

@Composable
private fun InitialHero(onChip: (String) -> Unit) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            painterResource(R.drawable.discover_logo),
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
        )
        Spacer(Modifier.height(20.dp))
        Text(
            "Hi! I’m Prologue your personal reading assistant. I can help you find the perfect books based on how you’re feeling, what you’re curious about or what you want to learn. What’s on your mind today?",
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Medium,
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(Modifier.height(24.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            QuickPromptChip("I had a stressful day") { onChip("I had a stressful day") }
            QuickPromptChip("I want to explore other worlds") { onChip("I want to explore other worlds") }
            QuickPromptChip("I feel uninspired") { onChip("I feel uninspired") }
        }
    }
}

@Composable
private fun DiscoverInputBar(
    value: String,
    onValueChange: (String) -> Unit,
    onSend: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        tonalElevation = 4.dp,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = {}) { Icon(Icons.Default.Add, null) }
            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.weight(1f),
                placeholder = { Text("Ask me anything") },
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Send),
                keyboardActions = KeyboardActions(onSend = { onSend() })
            )
        }
    }
}

@Composable
private fun ChatView(messages: List<ChatMessage>) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(messages, key = { it.id }) { msg ->
            ChatMessageBubble(isUser = msg.isUser, message = msg.text)
        }
    }
}

@SuppressLint("InvalidColorHexValue")
@Composable
private fun RecommendationsView(
    assistantMessage: String,
    books: List<RecommendationBookDto>,
    inLibrary: Set<String>,
    onAskAgain: () -> Unit,
    onAdd: (String) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                assistantMessage,
                maxLines = 5,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(Modifier.height(16.dp))
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Perfect for you Right Now", fontWeight = FontWeight.Bold)
                TextButton(onClick = onAskAgain) {
                    Text("Ask Again", color = Color(0xFF2B2D30))
                }
            }
            Divider()
        }
        items(books) { book ->
            RecommendationCard(book, inLibrary.contains(book.id), onAdd)
        }
    }
}

@Composable private fun CenterLoading() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable private fun ErrorView(msg: String) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(msg, color = MaterialTheme.colorScheme.error)
    }
}

@Composable
private fun PastChatsDrawer(onNew: () -> Unit, onSelect: (String) -> Unit) {
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("Conversations", fontWeight = FontWeight.SemiBold)
        Spacer(Modifier.height(8.dp))
        // For now static — can bind to ViewModel later
        repeat(3) {
            Text(
                "Chat ${it + 1}",
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onSelect("chat_${it + 1}") }
                    .padding(12.dp)
            )
        }
        Spacer(Modifier.weight(1f))
        Button(onClick = onNew, modifier = Modifier.fillMaxWidth()) {
            Text("New Chat")
        }
    }
}