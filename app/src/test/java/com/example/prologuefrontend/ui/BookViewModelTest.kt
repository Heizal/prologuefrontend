package com.example.prologuefrontend.ui

import com.example.prologuefrontend.data.model.Book
import com.example.prologuefrontend.data.model.ReadingState
import com.example.prologuefrontend.data.repository.BookRepository
import com.example.prologuefrontend.ui.viewmodels.BookState
import com.example.prologuefrontend.ui.viewmodels.BookViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class BookViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var repository: BookRepository
    private lateinit var viewModel: BookViewModel

    @Before
    fun setup() {
        repository = mockk()
    }

    @Test
    fun `init loads books and updates state to Success`() = runTest {
        // Given
        val mockBooks = listOf(
            Book(
                id = 1L,
                title = "The Great Gatsby",
                author = "F. Scott Fitzgerald",
                thumbnailUrl = "http://example.com/cover.jpg",
                progress = 45,
                readingState = ReadingState.CURRENTLY_READING
            )
        )

        coEvery { repository.getBooks() } returns mockBooks

        // When
        viewModel = BookViewModel(repository)

        advanceUntilIdle()

        // Then
        val state = viewModel.state.value
        assertTrue("Expected Success state", state is BookState.Success)
        assertEquals(mockBooks, (state as BookState.Success).books)
    }

    @Test
    fun `init handles error and updates state to Error`() = runTest {
        // Given
        coEvery { repository.getBooks() } throws RuntimeException("Network Failure")

        // When
        viewModel = BookViewModel(repository)
        advanceUntilIdle()

        // Then
        val state = viewModel.state.value
        assertTrue("Expected Error state", state is BookState.Error)
        assertEquals("Failed to fetch books", (state as BookState.Error).message)
    }
}