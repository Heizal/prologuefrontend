package com.example.prologuefrontend.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.prologuefrontend.data.model.Book
import com.example.prologuefrontend.ui.components.AIPicksSection
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
    val user by userViewModel.user.collectAsState()

    val username = user?.username ?: "Reader"

    HomeScreenContent(
        username = username,
        books = books,
        navController = navController
    )
}

@Composable
private fun HomeScreenContent(
    username: String,
    books: List<Book>,
    navController: NavHostController
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
            item { AIPicksSection() }
            item { RediscoverSection() }
            item { RecentActivitySection() }
            item { Spacer(Modifier.height(80.dp)) }
        }
    }
}


