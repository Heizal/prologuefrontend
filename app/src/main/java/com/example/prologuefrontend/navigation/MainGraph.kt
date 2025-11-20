package com.example.prologuefrontend.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.prologuefrontend.ui.components.BottomNavBar
import com.example.prologuefrontend.ui.screens.ChatDetailScreen
import com.example.prologuefrontend.ui.screens.DiscoverScreen
import com.example.prologuefrontend.ui.screens.HomeScreen
import com.example.prologuefrontend.ui.screens.MyBooksScreen
import com.example.prologuefrontend.ui.screens.ProfileScreen
import com.example.prologuefrontend.ui.viewmodels.AuthViewModel
import com.example.prologuefrontend.ui.viewmodels.DiscoverViewModel
import com.example.prologuefrontend.ui.viewmodels.UserViewModel

fun NavGraphBuilder.mainGraph(navController: NavHostController) {

    navigation(
        startDestination = "home",
        route = "main"
    ) {
        composable("home") { backStackEntry ->
            val userViewModel: UserViewModel = hiltViewModel(backStackEntry)
            LaunchedEffect(Unit) {
                userViewModel.loadUser()
            }

            MainScreenContainer(navController) {
                HomeScreen(
                    navController = navController,
                    userViewModel = userViewModel
                )
            }
        }

        composable("discover") {
            MainScreenContainer(navController) { DiscoverScreen(
                onChatSelected = { chatId ->
                    navController.navigate("chatDetail/$chatId")
                }
            ) }
        }

        composable("myBooks") {
            MainScreenContainer(navController) { MyBooksScreen() }
        }

        composable("profile") { backStackEntry ->
            val userViewModel: UserViewModel = hiltViewModel(backStackEntry)
            val authViewModel: AuthViewModel = hiltViewModel(backStackEntry)
            val userState by userViewModel.user.collectAsState()
            val discoverViewModel: DiscoverViewModel = hiltViewModel(backStackEntry)

            MainScreenContainer(navController) {
                ProfileScreen(
                    username = userState?.username ?: "Reader",
                    onLogoutClick = {
                        authViewModel.logout()
                        userViewModel.clear()
                        discoverViewModel.startNewChat()
                        navController.navigate("auth") {
                            popUpTo("main") { inclusive = true }
                        }
                    }
                )
            }
        }

        composable("chatDetail/{chatId}") { backStackEntry ->
            val chatId = backStackEntry.arguments?.getString("chatId")!!
            ChatDetailScreen(
                chatId = chatId,
                onChatSelected = { newChatId ->
                    navController.navigate("chatDetail/$newChatId") {
                        popUpTo("chatDetail/{chatId}") { inclusive = true }
                    }
                },
                onAskAgain = { prompt ->
                    navController.navigate("discover?prompt=$prompt")
                }
            )
        }
    }
}

@Composable
fun MainScreenContainer(
    navController: NavHostController,
    content: @Composable () -> Unit
) {
    Scaffold(
        bottomBar = { BottomNavBar(navController) }
    ) { inner ->
        androidx.compose.foundation.layout.Box(
            modifier = Modifier.padding(inner)
        ) {
            content()
        }
    }
}
