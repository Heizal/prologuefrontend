package com.example.prologuefrontend.ui.home

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.prologuefrontend.ui.components.AIPicksSection
import com.example.prologuefrontend.ui.components.BottomNavBar
import com.example.prologuefrontend.ui.components.CurrentReadingCard
import com.example.prologuefrontend.ui.components.GreetingSection
import com.example.prologuefrontend.ui.components.RecentActivitySection
import com.example.prologuefrontend.ui.components.RediscoverSection

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun HomeScreen(homeViewModel: HomeViewModel = viewModel()){
    val books by homeViewModel.books.collectAsState()

    Scaffold(
        bottomBar = {BottomNavBar()},
        containerColor = MaterialTheme.colorScheme.background,
    ) { innerPadding ->
            LazyColumn(
                contentPadding = innerPadding,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 20.dp)
            ) {
                item{GreetingSection(username = "Heizal")}
                item{Spacer(Modifier.height(16.dp))}

                item { CurrentReadingCard() }
                item { Spacer(Modifier.height(24.dp)) }

                item { AIPicksSection() }
                item { Spacer(Modifier.height(24.dp)) }

                item { RediscoverSection() }
                item { Spacer(Modifier.height(24.dp)) }

                item { RecentActivitySection() }
                item { Spacer(Modifier.height(80.dp)) }
            }
    }
}
