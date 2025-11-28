package com.example.prologuefrontend.ui

import androidx.compose.foundation.layout.size
import androidx.compose.ui.semantics.error

import com.example.prologuefrontend.data.model.ChatDetail
import com.example.prologuefrontend.data.model.ChatPreview
import com.example.prologuefrontend.data.model.DetailScreenState
import com.example.prologuefrontend.data.repository.ChatRepository
import com.example.prologuefrontend.ui.viewmodels.ChatDetailViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.DefaultAsserter.assertTrue
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class ChatDetailViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var repository: ChatRepository
    private lateinit var viewModel: ChatDetailViewModel

    @Before
    fun setup() {
        repository = mockk()
        viewModel = ChatDetailViewModel(repository)
    }
    @Test
    fun `loadChatDetail updates state to Loaded with success data`() = runTest {
        // Given
        val chatId = "chat_123"
        val mockDetail = ChatDetail(
            chatId = chatId,
            userMessage = "Can you recommend a sci-fi book?",
            modelResponse = "Sure! Try Dune.",
            recommendations = emptyList()
        )

        coEvery { repository.getChatDetail(chatId) } returns mockDetail

        // When
        viewModel.loadChatDetail(chatId)

        advanceUntilIdle()

        // Then
        val currentState = viewModel.state.value.screen
        assertTrue("Expected state to be Loaded", currentState is DetailScreenState.Loaded)
        assertEquals(mockDetail, (currentState as DetailScreenState.Loaded).detail)
    }

    @Test
    fun `loadChatDetail updates state to Error on failure`() = runTest {
        // Given
        val chatId = "chat_123"
        val errorMessage = "Network error"

        coEvery { repository.getChatDetail(chatId) } throws RuntimeException(errorMessage)

        // When
        viewModel.loadChatDetail(chatId)
        advanceUntilIdle()

        // Then
        val currentState = viewModel.state.value.screen
        assertTrue("Expected state to be Error", currentState is DetailScreenState.Error)
        assertEquals(errorMessage, (currentState as DetailScreenState.Error).message)
    }

    @Test
    fun `loadChatPreviews updates sidebar state successfully`() = runTest {
        // Given
        val mockPreviews = listOf(
            ChatPreview("1", "Chat A", "2023-01-01", null),
            ChatPreview("2", "Chat B", "2023-01-02", null)
        )

        coEvery { repository.getChatPreviews() } returns mockPreviews

        // When
        viewModel.loadChatPreviews()
        advanceUntilIdle()

        // Then
        val sidebarState = viewModel.state.value.sidebar
        assertEquals(mockPreviews, sidebarState.previews)
        assertFalse("Sidebar loading should be false", sidebarState.isLoading)
        assertNull("Sidebar error should be null", sidebarState.error)
    }

    @Test
    fun `loadChatPreviews handles empty list`() = runTest {
        // Given
        coEvery { repository.getChatPreviews() } returns emptyList()

        // When
        viewModel.loadChatPreviews()
        advanceUntilIdle()

        // Then
        val sidebarState = viewModel.state.value.sidebar
        assertEquals(0, sidebarState.previews.size)
        assertFalse(sidebarState.isLoading)
    }

    @Test
    fun `loadChatPreviews sets specific sidebar error on failure`() = runTest {
        // Given
        val errorMsg = "Failed to load history"
        coEvery { repository.getChatPreviews() } throws RuntimeException(errorMsg)

        // When
        viewModel.loadChatPreviews()
        advanceUntilIdle()

        // Then
        val sidebarState = viewModel.state.value.sidebar
        assertFalse(sidebarState.isLoading)
        assertEquals(errorMsg, sidebarState.error)
        val screenState = viewModel.state.value.screen
        assertTrue(screenState !is DetailScreenState.Error)
    }
}
