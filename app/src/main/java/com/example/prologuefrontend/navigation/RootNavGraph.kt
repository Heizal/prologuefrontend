package com.example.prologuefrontend.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.example.prologuefrontend.ui.viewmodels.MainViewModel

@Composable
fun RootNavGraph(
    navController: NavHostController,
    mainViewModel: MainViewModel = hiltViewModel()
) {
    val startDest by mainViewModel.startDestination.collectAsState()

    if (startDest != null) {
        NavHost(
            navController = navController,
            startDestination = startDest!!
        ) {
            authGraph(navController)
            mainGraph(navController)
        }
    }
}