package com.example.prologuefrontend.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun BottomNavBar() {
    NavigationBar {
        NavigationBarItem(
            selected = true,
            onClick = {},
            icon = { Icon(Icons.Default.Home, null) },
            label = { Text("Home") }
        )
        NavigationBarItem(
            selected = false,
            onClick = {},
            icon = { Icon(Icons.Default.FavoriteBorder, null) },
            label = { Text("My Books") }
        )
        NavigationBarItem(
            selected = false,
            onClick = {},
            icon = { Icon(Icons.Default.Search, null) },
            label = { Text("Discover") }
        )
        NavigationBarItem(
            selected = false,
            onClick = {},
            icon = { Icon(Icons.Default.Person, null) },
            label = { Text("Profile") }
        )
    }
}
