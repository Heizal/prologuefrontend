package com.example.prologuefrontend.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.prologuefrontend.R
import com.example.prologuefrontend.ui.components.QuickPromptChip
import com.example.prologuefrontend.ui.components.RecommendationCard
import com.example.prologuefrontend.ui.viewmodels.DiscoverUiState
import com.example.prologuefrontend.ui.viewmodels.DiscoverViewModel
import kotlinx.coroutines.launch


val EBGaramond = FontFamily(Font(R.font.eb_garamond_regular))
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiscoverScreen(vm: DiscoverViewModel = hiltViewModel()) {
    val state by vm.uiState.collectAsState()
    var input by remember { mutableStateOf("") }
    val listState = rememberLazyListState()
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
        Scaffold(
            topBar = { DiscoverTopBar(onNewChatClick = { vm.startNewChat() }) },
            containerColor = Color.White
        ) { innerPadding ->

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(Color.White)
            ) {
                DiscoverContent(state = state, vm = vm, listState = listState)
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
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DiscoverTopBar(onNewChatClick: () -> Unit) {
    Surface(
        color = Color.White,
        tonalElevation = 2.dp,
        shadowElevation = 2.dp
    ) {
        TopAppBar(
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        "Prologue ",
                        fontFamily = EBGaramond,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = Color.Black
                    )
                    Text(
                        ".",
                        fontFamily = EBGaramond,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = Color(0xFF4D884F)
                    )
                }
            },
            actions = {
                IconButton(onClick = {}) {
                    Icon(
                        Icons.Outlined.Notifications,
                        contentDescription = "Notifications",
                        tint = Color.Black
                    )
                }
                IconButton(onClick = onNewChatClick) {
                    Icon(
                        Icons.Outlined.Edit,
                        contentDescription = "New Chat",
                        tint = Color.Black
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.White,
                titleContentColor = Color.Black
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .padding(horizontal = 12.dp)
        )

    }

}

@Composable
private fun DiscoverContent(
    state: DiscoverUiState,
    vm: DiscoverViewModel,
    listState: androidx.compose.foundation.lazy.LazyListState
) {
    LazyColumn(
        state = listState,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        when (state) {
            DiscoverUiState.Initial -> item { InitialDiscoverLayout(onChip = vm::selectQuickPrompt) }

            is DiscoverUiState.Recommendations -> {
                val rec = state
                item {
                    vm.lastUserPrompt?.let {
                        UserBubble(it)
                        Spacer(Modifier.height(8.dp))
                    }

                    Text(
                        rec.assistantMessage,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Black
                    )
                    Spacer(Modifier.height(16.dp))
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "Perfect for you Right Now",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        TextButton(onClick = vm::askAgain) {
                            Text("Ask Again", color = Color(0xFF4D884F))
                        }
                    }
                    Divider()
                }
                items(rec.books) { book ->
                    RecommendationCard(
                        book = book,
                        rec.inLibrary.contains(book.id),
                        { bookId ->
                            val bookToAdd = rec.books.find { it.id == bookId }
                            if (bookToAdd != null) {
                                vm.addBook(bookToAdd)
                            }
                        }
                    )
                }
            }

            DiscoverUiState.Loading -> item { CenterLoading() }
            is DiscoverUiState.Error -> item { ErrorView(state.message) }
            else -> Unit
        }
    }
}

@Composable
private fun InitialDiscoverLayout(onChip: (String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(40.dp))
        Icon(
            painterResource(R.drawable.discover_logo),
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
        )
        Spacer(Modifier.height(24.dp))
        Text(
            "Hi! I’m Prologue your personal reading assistant. I can help you find the perfect books based on how you’re feeling, what you’re curious about or what you want to learn. What’s on your mind today?",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium
        )
        Spacer(Modifier.height(32.dp))
        // Chips ABOVE the input bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            QuickPromptChip("I had a stressful day") { onChip("I had a stressful day") }
            QuickPromptChip("I want to explore other worlds") { onChip("I want to explore other worlds") }
            QuickPromptChip("I feel uninspired") { onChip("I feel uninspired") }
        }
        Spacer(Modifier.height(140.dp))
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
        tonalElevation = 0.dp,
        color = Color.White,
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = {}) { Icon(Icons.Default.Add, null, tint = Color.Black) }
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
private fun UserBubble(message: String) {
    Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
        Text(
            text = message,
            color = Color.Black,
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xFFDCD2FC))
                .padding(horizontal = 14.dp, vertical = 8.dp)
        )
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
        Button(onClick = onNew, modifier = Modifier.fillMaxWidth()) { Text("New Chat") }
    }
}