package com.example.prologuefrontend.ui.screens

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import com.example.prologuefrontend.data.model.MyBooksUiState
import com.example.prologuefrontend.data.model.ReadingState
import com.example.prologuefrontend.ui.viewmodels.MyBooksViewModel

@Composable
fun MyBooksScreen(viewModel: MyBooksViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()

    val context = LocalContext.current


    val onBookClick: (String?) -> Unit = { link ->
        if (!link.isNullOrBlank()) {
            try {
                val uri = Uri.parse(link)
                val intent = Intent(Intent.ACTION_VIEW).apply {
                    setDataAndType(uri, "application/pdf") // Defaulting to PDF, change if you support EPUBs
                    flags = Intent.FLAG_ACTIVITY_NO_HISTORY or Intent.FLAG_GRANT_READ_URI_PERMISSION
                }

                // Create a chooser so the user can pick their preferred app
                val chooser = Intent.createChooser(intent, "Open book with")
                context.startActivity(chooser)
            } catch (e: Exception) {
                // Fallback: Try opening as a generic web link if no PDF viewer is found
                try {
                    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
                    context.startActivity(browserIntent)
                } catch (e2: Exception) {
                    Toast.makeText(context, "Could not open book", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Toast.makeText(context, "File path not found", Toast.LENGTH_SHORT).show()
        }
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let { viewModel.uploadBook(it) }
    }

    // 2. Pass state and events to the stateless composable
    MyBooksContent(
        uiState = uiState,
        onQueryChange = viewModel::onSearchQueryChange,
        onUploadClick = { launcher.launch("*/*") },
        onBookClick = onBookClick
    )
}
@Composable
fun MyBooksContent(
    uiState: MyBooksUiState,
    onQueryChange: (String) -> Unit,
    onUploadClick: () -> Unit,
    onBookClick: (String?) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        MyBooksSearchBarWithAddButton(
            query = uiState.query,
            onQueryChange = onQueryChange,
            onSearch = onQueryChange,
            onUploadClick = onUploadClick
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Handle Loading
        if (uiState.isLoading && uiState.books.isEmpty()) {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        // Handle Error
        if (uiState.error != null && !uiState.isLoading) {
            Text(
                text = uiState.error,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(8.dp)
            )
        }

        // Handle Content
        if (uiState.books.isEmpty() && !uiState.isLoading && uiState.error == null) {
            if (uiState.query.isNotEmpty()) {
                Text(
                    text = "No results found for '${uiState.query}'",
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            } else {
                EmptyLibrary(onUpload = onUploadClick)
            }
        } else {
            BookSection(
                title = "Currently Reading",
                books = uiState.books.filter { it.readingState == ReadingState.CURRENTLY_READING },
                onBookClick = onBookClick
            )
            Spacer(modifier = Modifier.height(12.dp))
            VerticalBookSection(
                title = "Want to Read",
                books = uiState.books.filter { it.readingState == ReadingState.WANT_TO_READ },
                onBookClick = onBookClick
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
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        SearchBar(
            query = query,
            onQueryChange = onQueryChange,
            onSearch = onSearch,
            active = false,
            onActiveChange = { },
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
fun BookSection(title: String, books: List<Book>, onBookClick: (String?) -> Unit) {
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
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            items(books.size) { index ->
                BookCard(books[index], modifier = Modifier.width(200.dp), onBookClick = {onBookClick(books[index].infoLink)})
            }
        }
    }
}

@Composable
fun VerticalBookSection(title: String, books: List<Book>, onBookClick: (String?) -> Unit) {
    if (books.isEmpty()) return

    Column(modifier = Modifier.fillMaxWidth()) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }
        Column(modifier = Modifier.padding(vertical = 8.dp)) {
            books.chunked(2).forEach { rowBooks ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    rowBooks.forEach { book ->
                        BookCard(book = book, modifier = Modifier.weight(1f), onBookClick = {onBookClick(book.infoLink)} )
                    }
                    if (rowBooks.size == 1) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
fun BookCard(
    book: Book,
    modifier: Modifier = Modifier,
    onBookClick: (String?) -> Unit
) {
    val painter = rememberAsyncImagePainter(
        placeholder = painterResource(R.drawable.default_cover),
        model = book.thumbnailUrl ?: R.drawable.default_cover,
        error = painterResource(R.drawable.default_cover)
    )
    Card(
        modifier = modifier
            .padding(4.dp)
            .clickable { onBookClick(book.infoLink) },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column {
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


