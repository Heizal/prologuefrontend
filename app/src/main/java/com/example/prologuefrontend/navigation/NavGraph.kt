package com.example.prologuefrontend.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.prologuefrontend.ui.components.BottomNavBar
import com.example.prologuefrontend.ui.screens.DiscoverScreen
import com.example.prologuefrontend.ui.screens.HomeScreen
import com.example.prologuefrontend.ui.screens.MyBooksScreen

@Composable
fun NavGraph(navController: NavHostController = rememberNavController()) {
    Scaffold (
        bottomBar = { BottomNavBar(navController) }
    ){ inner ->
        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(inner)
        ){
            composable("home") { HomeScreen(navController) }
            composable ("discover" ){DiscoverScreen()}
            composable ("myBooks"){ MyBooksScreen() }
        }

    }
}