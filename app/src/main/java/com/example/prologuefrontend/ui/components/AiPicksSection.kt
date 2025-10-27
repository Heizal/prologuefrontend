package com.example.prologuefrontend.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.prologuefrontend.ui.viewmodels.AIPickState
import com.example.prologuefrontend.ui.viewmodels.AIPickViewModel
import com.example.prologuefrontend.data.model.AIPick


@Composable
fun AIPicksSection(viewModel: AIPickViewModel = viewModel()) {
    val state by viewModel.state.collectAsState()

    Column {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("AI picks for You", fontWeight = FontWeight.Bold)
            Text("More", color = Color.Gray)
        }
        Spacer(Modifier.height(8.dp))
        when (state){
            is AIPickState.Loading ->{
                Text("Loading...", color = Color.Gray)
            }
            is AIPickState.Error ->{
                Text("Error loading AI picks", color = Color.Red)
            }
            is AIPickState.Success ->{
                val pick = (state as AIPickState.Success).data
                AIPickCard(pick)
            }
        }
    }
}


