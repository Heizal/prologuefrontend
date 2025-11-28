package com.example.prologuefrontend.ui

import android.content.Context
import android.net.Uri
import com.example.prologuefrontend.data.model.Book
import com.example.prologuefrontend.data.model.ReadingState
import com.example.prologuefrontend.data.repository.BookRepository
import com.example.prologuefrontend.ui.viewmodels.MyBooksViewModel
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.File

@OptIn(ExperimentalCoroutinesApi::class)
class MyBooksViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var repository: BookRepository
    private lateinit var context: Context
    private lateinit var viewModel: MyBooksViewModel

    private val book1 = Book(1L, "Dune", "Frank Herbert", null, 0, ReadingState.WANT_TO_READ)
    private val book2 = Book(2L, "Harry Potter", "Rowling", null, 0, ReadingState.WANT_TO_READ)

    @Before
    fun setup() {
        repository = mockk()
        context = mockk()
    }

    @Test
    fun `onSearchQueryChange filters books locally`() = runTest {
        // Given
        val allBooks = listOf(book1, book2)
        coEvery { repository.getBooks(null) } returns allBooks

        viewModel = MyBooksViewModel(repository, context)
        advanceUntilIdle()

        // When
        viewModel.onSearchQueryChange("Dune")

        // Then
        val state = viewModel.uiState.value
        assertEquals(1, state.books.size)
        assertEquals("Dune", state.books.first().title)
    }

    @Test
    fun `uploadBook uses context cacheDir`() = runTest {
        val mockUri = mockk<Uri>()
        val mockFile = mockk<File>()

        every { context.cacheDir } returns mockFile
        coEvery { repository.getBooks(null) } returns emptyList()
        coEvery { repository.uploadBook(any(), any()) } returns book1

        viewModel = MyBooksViewModel(repository, context)
        viewModel.uploadBook(mockUri)
        advanceUntilIdle()

        // Verify the repo received the file from context
        io.mockk.coVerify { repository.uploadBook(mockUri, mockFile) }
    }
}