package com.example.prologuefrontend.ui.screens

import android.content.Intent
import android.net.Uri
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.prologuefrontend.R
import com.example.prologuefrontend.data.model.ScreenState
import com.example.prologuefrontend.ui.components.PastChatsDrawer
import com.example.prologuefrontend.ui.components.QuickPromptChip
import com.example.prologuefrontend.ui.components.RecommendationCard
import com.example.prologuefrontend.ui.viewmodels.DiscoverViewModel
import kotlinx.coroutines.launch

val EBGaramond = FontFamily(Font(R.font.eb_garamond_regular))

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiscoverScreen(
    vm: DiscoverViewModel = hiltViewModel(),
    onChatSelected: (String) -> Unit
) {
    val state by vm.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        vm.loadChatPreviews()
    }

    var input by remember { mutableStateOf("") }
    val listState = rememberLazyListState()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            PastChatsDrawer(
                previews = state.sidebar.previews,
                isLoading = state.sidebar.isLoading,
                error = state.sidebar.error,
                onNew = {
                    vm.startNewChat()
                    scope.launch { drawerState.close() }
                },
                onSelect = { id ->
                    onChatSelected(id)
                    scope.launch { drawerState.close() }
                }
            )
        }
    ) {
        Scaffold(
            topBar = {
                DiscoverTopBar(
                    onNewChatClick = { vm.startNewChat() },
                    onMenuClick = { scope.launch { drawerState.open() } }
                )
            },
            containerColor = Color.White
        ) { innerPadding ->

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(Color.White)
            ) {
                DiscoverContent(
                    screen = state.screen,
                    vm = vm,
                    listState = listState
                )

                if (state.screen is ScreenState.Initial) {
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
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DiscoverTopBar(onNewChatClick: () -> Unit, onMenuClick: () -> Unit) {
    TopAppBar(
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    "Prologue",
                    fontFamily = EBGaramond,
                    fontWeight = FontWeight.Bold,
                    fontSize = 26.sp,
                    color = Color.Black
                )
                Text(
                    ".",
                    fontFamily = EBGaramond,
                    fontWeight = FontWeight.Bold,
                    fontSize = 26.sp,
                    color = Color(0xFF4D884F)
                )
            }
        },
        navigationIcon = {
            IconButton(onClick = onMenuClick) {
                Icon(Icons.Default.Menu, contentDescription = "History", tint = Color.Black)
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
            containerColor = Color.White
        ),
        modifier = Modifier.padding(horizontal = 4.dp)
    )
}

@Composable
private fun DiscoverContent(
    screen: ScreenState,
    vm: DiscoverViewModel,
    listState: LazyListState
) {
    val context = LocalContext.current
    when (screen) {

        is ScreenState.Initial -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 50.dp)
            ) {
                InitialDiscoverLayout(
                    onChip = vm::selectQuickPrompt,
                    modifier = Modifier.align(Alignment.BottomCenter)
                )
            }
        }

        is ScreenState.Loading -> {
            CenterLoading()
        }

        is ScreenState.Error -> {
            ErrorView(screen.message)
        }

        is ScreenState.Recommendations -> {
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                item {
                    UserBubble(screen.prompt)
                    Spacer(Modifier.height(8.dp))

                    Text(
                        screen.responseMessage,
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

                items(screen.books) { book ->
                    RecommendationCard(
                        book = book,
                        isInLibrary = screen.inLibrary.contains(book.title.lowercase() + "|" + book.author.lowercase()) ,
                        onAddClick = { vm.addBook(it) },
                        onBookClick = { url ->
                            if (!url.isNullOrBlank()) {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                                context.startActivity(intent)
                            }
                        }
                    )
                }

                item { Spacer(Modifier.height(80.dp)) }
            }
        }

        is ScreenState.Chat -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Conversation view not implemented yet")
            }
        }
    }
}


@Composable
private fun InitialDiscoverLayout(onChip: (String) -> Unit, modifier: Modifier) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
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
        Spacer(Modifier.height(24.dp))
        Text(
            "Hi! I’m Prologue your personal reading assistant. I can help you find the perfect books based on how you’re feeling, what you’re curious about or what you want to learn. What’s on your mind today?",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium,
            fontFamily = EBGaramond
        )
        Spacer(Modifier.height(32.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            QuickPromptChip("I had a stressful day") { onChip("I had a stressful day") }
            QuickPromptChip("I want to explore other worlds") { onChip("I want to explore other worlds") }
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
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(elevation = 4.dp, shape = RoundedCornerShape(32.dp))
                .background(Color.White, shape = RoundedCornerShape(32.dp))
                .padding(horizontal = 20.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Box(modifier = Modifier.weight(1f)) {
                if (value.isEmpty()) {
                    Text(
                        text = "Ask me anything",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.LightGray
                    )
                }
                BasicTextField(
                    value = value,
                    onValueChange = onValueChange,
                    textStyle = MaterialTheme.typography.bodyLarge.copy(color = Color.Black),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Send),
                    keyboardActions = KeyboardActions(onSend = { onSend() })
                )
            }

            Spacer(Modifier.width(8.dp))

            // The Send Button
            IconButton(
                onClick = onSend,
                modifier = Modifier.size(24.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Send,
                    contentDescription = "Send",
                    tint = Color(0xFF4D884F)
                )
            }
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
