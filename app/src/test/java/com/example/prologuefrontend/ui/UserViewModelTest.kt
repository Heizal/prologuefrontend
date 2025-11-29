package com.example.prologuefrontend.ui

import com.example.prologuefrontend.data.model.UserResponse
import com.example.prologuefrontend.data.repository.UserRepository
import com.example.prologuefrontend.ui.viewmodels.UserViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class UserViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var repository: UserRepository
    private lateinit var viewModel: UserViewModel

    @Before
    fun setup() {
        repository = mockk()
        viewModel = UserViewModel(repository)
    }

    @Test
    fun `loadUser updates state with user data on success`() = runTest {
        // Given
        val mockUser = UserResponse(
            id = 123L, // Long type
            email = "test@example.com",
            username = "test_user",
            createdAt = "2025-01-01T12:00:00Z",
            bio = "Hello World",
            profilePictureUrl = null
        )

        coEvery { repository.getMe() } returns mockUser

        // When
        viewModel.loadUser()
        advanceUntilIdle() // Wait for coroutine to finish

        // Then
        assertEquals(mockUser, viewModel.user.value)
    }

    @Test
    fun `loadUser handles error gracefully`() = runTest {
        // Given
        coEvery { repository.getMe() } throws RuntimeException("Network Error")

        // When
        viewModel.loadUser()
        advanceUntilIdle()

        // Then: State should remain null (assuming ViewModel catches exception and logs it)
        assertNull(viewModel.user.value)
    }

    @Test
    fun `clear resets user state to null`() = runTest {
        // Given: A user is currently loaded
        val mockUser = UserResponse(
            id = 1L,
            email = "a@a.com",
            username = "a",
            createdAt = "date"
        )
        coEvery { repository.getMe() } returns mockUser

        viewModel.loadUser()
        advanceUntilIdle()

        // Pre-check
        assertEquals(mockUser, viewModel.user.value)

        // When
        viewModel.clear()

        // Then
        assertNull(viewModel.user.value)
    }
}