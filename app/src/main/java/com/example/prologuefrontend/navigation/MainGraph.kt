package com.example.prologuefrontend.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.example.prologuefrontend.ui.components.BottomNavBar
import com.example.prologuefrontend.ui.screens.DiscoverScreen
import com.example.prologuefrontend.ui.screens.HomeScreen
import com.example.prologuefrontend.ui.screens.MyBooksScreen

fun NavGraphBuilder.mainGraph(navController: NavHostController) {

    navigation(
        startDestination = "home",
        route = "main"
    ) {
        composable("home") {
            MainScreenContainer(navController) { HomeScreen(navController) }
        }

        composable("discover") {
            MainScreenContainer(navController) { DiscoverScreen() }
        }

        composable("myBooks") {
            MainScreenContainer(navController) { MyBooksScreen() }
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
