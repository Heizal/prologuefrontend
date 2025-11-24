package com.example.prologuefrontend.ui.screens

import AIPicksSection
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.prologuefrontend.data.model.Book
import com.example.prologuefrontend.data.model.HomeUiState
import com.example.prologuefrontend.ui.components.CurrentReadingCard
import com.example.prologuefrontend.ui.components.GreetingSection
import com.example.prologuefrontend.ui.components.NewUserAIPicksCard
import com.example.prologuefrontend.ui.components.NewUserCurrentlyReadingCard
import com.example.prologuefrontend.ui.components.RecentActivitySection
import com.example.prologuefrontend.ui.components.RediscoverSection
import com.example.prologuefrontend.ui.viewmodels.HomeViewModel
import com.example.prologuefrontend.ui.viewmodels.UserViewModel

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun HomeScreen(
    navController: NavHostController,
    userViewModel: UserViewModel
) {
    val homeViewModel: HomeViewModel = hiltViewModel()
    val books by homeViewModel.books.collectAsState()
    val homeState by homeViewModel.uiState.collectAsState()
    val user by userViewModel.user.collectAsState()

    val username = user?.username ?: "Reader"

    Scaffold(
        topBar = {
            HomeTopBar(
                onProfileClick = { navController.navigate("profile") }
            )
        },
        containerColor = Color.White
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            HomeScreenContent(
                username = username,
                books = books,
                navController = navController,
                homeState = homeState
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeTopBar(
    onProfileClick: () -> Unit
) {
    TopAppBar(
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    "Prologue",
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 26.sp,
                    color = Color.Black
                )
                Text(
                    ".",
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 26.sp,
                    color = Color(0xFF4D884F)
                )
            }
        },
        actions = {
            IconButton(onClick = {}) {
                Icon(
                    imageVector = Icons.Outlined.Notifications,
                    contentDescription = "Notifications",
                    tint = Color.Black
                )
            }
            IconButton(onClick = onProfileClick) {
                Icon(
                    imageVector = Icons.Outlined.Person,
                    contentDescription = "Profile",
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
private fun HomeScreenContent(
    username: String,
    books: List<Book>,
    navController: NavHostController,
    homeState: HomeUiState
) {
    val isNewUser = books.isEmpty()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item { GreetingSection(username) }

        if (isNewUser) {
            item {
                NewUserCurrentlyReadingCard(
                    onStartReadingClick = { navController.navigate("myBooks") }
                )
            }

            item {
                NewUserAIPicksCard(
                    onDiscoverClick = { navController.navigate("discover") }
                )
            }
            item { Spacer(Modifier.height(80.dp)) }

        } else {
            item {
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    items(books) { book ->
                        CurrentReadingCard(book = book)
                    }
                }
            }
            item { AIPicksSection(uiState = homeState.aiPick) }
            item { RediscoverSection() }
            item { RecentActivitySection() }
            item { Spacer(Modifier.height(80.dp)) }
        }
    }
}


