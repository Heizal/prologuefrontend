package com.example.prologuefrontend.ui.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.rememberAsyncImagePainter
import com.example.prologuefrontend.R
import com.example.prologuefrontend.data.model.Book
import com.example.prologuefrontend.data.model.ReadingState
import com.example.prologuefrontend.ui.viewmodels.MyBooksViewModel

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun MyBooksScreen(viewModel: MyBooksViewModel = hiltViewModel()) {
    val books by viewModel.books.collectAsState()
    val query by viewModel.query.collectAsState()
    val context = LocalContext.current
    var active by remember { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let { viewModel.uploadBook(it) }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        MyBooksSearchBarWithAddButton(
            query = query,
            onQueryChange = viewModel::onSearchQueryChange,
            onSearch = { viewModel.onSearchQueryChange(it) },
            onUploadClick = { launcher.launch("*/*") }
        )

        Spacer(modifier = Modifier.height(24.dp))

        if (books.isEmpty()) {
            EmptyLibrary(onUpload = {
                launcher.launch("*/*")
            })
        } else {
            BookSection(
                title = "Currently Reading",
                books = books.filter { it.readingState == ReadingState.CURRENTLY_READING}
            )
            Spacer(modifier = Modifier.height(12.dp))
            BookSection(
                title = "Want to Read",
                books = books.filter { it.readingState == ReadingState.WANT_TO_READ }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyBooksSearchBarWithAddButton(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    onUploadClick: () -> Unit
) {
    var active by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        SearchBar(
            query = query,
            onQueryChange = onQueryChange,
            onSearch = onSearch,
            active = active,
            onActiveChange = { active = it },
            placeholder = { Text("Search your library...") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = SearchBarDefaults.colors(containerColor = Color(0xFFDCDCDC)),
            trailingIcon = {
                IconButton(
                    onClick = onUploadClick,
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .size(40.dp)
                        .background(Color(0xF3D2D2D5), CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add Book",
                        tint = Color.Black
                    )
                }
            }
        ) {}
    }
}


@Composable
fun EmptyLibrary(onUpload: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Upload your books to get started!", fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))
        Button(onClick = onUpload) {
            Text("Browse Files")
        }
    }
}

@Composable
fun BookSection(title: String, books: List<Book>) {
    if (books.isEmpty()) return

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                style= MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Text(
                text = "More",
                color = Color(0xFF4D884F),
                fontWeight = FontWeight.Medium
            )
        }

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            items(books.size) { index ->
                BookCard(books[index])
            }
        }
    }
}


@Composable
fun BookCard(book: Book) {
    val painter = rememberAsyncImagePainter(
        placeholder = painterResource(R.drawable.default_cover),
        model = book.thumbnailUrl ?: R.drawable.default_cover,
        error = painterResource(R.drawable.default_cover)
    )
    Card(
        modifier = Modifier
            .width(200.dp)
            .padding(4.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
        ) {
            Image(
                painter = painter,
                contentDescription = book.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .height(180.dp)
                    .fillMaxWidth()
            )
            Column(modifier = Modifier.padding(8.dp)) {
                Text(
                    text = book.title,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = book.author,
                        fontWeight = FontWeight.Light,
                        maxLines = 1
                    )
                    if (book.progress > 0) {
                        Text(
                            text = " . ${book.progress.toInt()}%",
                            fontWeight = FontWeight.Light,
                            maxLines = 1
                        )
                    }
                }
            }
        }
    }
}
