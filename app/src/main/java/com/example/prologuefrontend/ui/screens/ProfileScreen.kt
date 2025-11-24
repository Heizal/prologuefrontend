package com.example.prologuefrontend.ui.screens

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.example.prologuefrontend.data.model.ProfileUiState
import com.example.prologuefrontend.ui.viewmodels.ProfileViewModel
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

private const val BASE_URL = "http://10.0.2.2:8080"


@Composable
fun ProfileScreen(
    onLogoutClick: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadProfile()
    }

    val context = LocalContext.current

    val avatarPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            val cacheDir = context.cacheDir
            val inputStream = context.contentResolver.openInputStream(uri)
            if (inputStream != null) {
                val tempFile = File(cacheDir, "avatar_${System.currentTimeMillis()}.png")
                tempFile.outputStream().use { output ->
                    inputStream.copyTo(output)
                }

                val requestFile = tempFile.asRequestBody("image/*".toMediaTypeOrNull())
                val part = MultipartBody.Part.createFormData(
                    "file",
                    tempFile.name,
                    requestFile
                )

                viewModel.uploadAvatar(part)
            }
        }
    }

    Scaffold(
        containerColor = Color.White
    ) { innerPadding ->
        ProfileContent(
            state = state,
            onBioChange = viewModel::updateBio,
            onSaveProfile = viewModel::saveProfile,
            onChangeAvatarClick = {
                avatarPicker.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
            },
            onLogoutClick = onLogoutClick,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Composable
private fun ProfileContent(
    state: ProfileUiState,
    onBioChange: (String) -> Unit,
    onSaveProfile: () -> Unit,
    onChangeAvatarClick: () -> Unit,
    onLogoutClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(40.dp))

        Box(
            contentAlignment = Alignment.BottomEnd,
            modifier = Modifier
                .size(120.dp)
                .clickable { onChangeAvatarClick() }
        ) {
            if (state.profilePictureUrl != null) {
                val fullUrl = if (state.profilePictureUrl.startsWith("/")) {
                    "$BASE_URL${state.profilePictureUrl}"
                } else {
                    state.profilePictureUrl
                }
                AsyncImage(
                    model = fullUrl,
                    contentDescription = "Profile picture",
                    contentScale = ContentScale.Crop,
                    onLoading = { Log.d("ProfileImage", "Loading URL: ${state.profilePictureUrl}") },
                    onError = { result ->
                        Log.e("ProfileImage", "Error loading image: ${result.result.throwable.message}")
                    },
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                        .border(2.dp, Color(0xFFE0E0E0), CircleShape)
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFFF0F0F0), CircleShape)
                        .border(2.dp, Color(0xFFE0E0E0), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = state.username.firstOrNull()?.uppercase() ?: "R",
                        style = MaterialTheme.typography.displayMedium,
                        color = Color.Gray,
                        fontFamily = FontFamily.Serif
                    )
                }
            }
            Box(
                modifier = Modifier
                    .offset(x = 4.dp, y = 4.dp)
                    .size(36.dp)
                    .background(Color.Black, CircleShape)
                    .border(2.dp, Color.White, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.AddCircle,
                    contentDescription = "Edit Avatar",
                    tint = Color.White,
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        Spacer(Modifier.height(24.dp))

        Text(
            text = state.username,
            style = MaterialTheme.typography.headlineMedium.copy(
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold
            ),
            color = Color.Black
        )

        TextButton(onClick = onSaveProfile) {
            Text("Save Changes", color = Color(0xFF4D884F))
        }

        Spacer(Modifier.height(32.dp))

        HorizontalDivider(color = Color(0xFFF0F0F0))
        Spacer(Modifier.height(32.dp))

        Text(
            text = "Reading Statistics",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.align(Alignment.Start)
        )

        Spacer(Modifier.height(16.dp))

        Card(
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFAFAFA)),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                ProfileStat("Books Read", state.booksRead.toString())
                ProfileStat("Reading", state.currentlyReading.toString())
                ProfileStat("To Read", state.wantToRead.toString())
            }
        }

        Spacer(Modifier.weight(1f))

        Button(
            onClick = onLogoutClick,
            colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
            shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text("Log out", color = Color.White, fontSize = 16.sp)
        }

        Spacer(Modifier.height(32.dp))

        if (state.error != null) {
            Text(
                text = state.error!!,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }
    }
}

@Composable
fun ProfileStat(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Serif
            ),
            color = Color.Black
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray
        )
    }
}