package com.example.prologuefrontend.ui

import com.example.prologuefrontend.data.model.ProfileStats
import com.example.prologuefrontend.data.model.UserResponse
import com.example.prologuefrontend.data.repository.BookRepository
import com.example.prologuefrontend.data.repository.UserRepository
import com.example.prologuefrontend.ui.viewmodels.ProfileViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import okhttp3.MultipartBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ProfileViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var userRepository: UserRepository
    private lateinit var bookRepository: BookRepository
    private lateinit var viewModel: ProfileViewModel

    // Dummy Data
    private val mockUser = UserResponse(
        id = 1L,
        email = "test@test.com",
        username = "testuser",
        createdAt = "2023-01-01",
        bio = "Old Bio",
        profilePictureUrl = "http://old.jpg"
    )

    private val mockStats = ProfileStats(
        booksRead = 10,
        currentlyReading = 2,
        wantToRead = 5
    )

    @Before
    fun setup() {
        userRepository = mockk()
        bookRepository = mockk()
        viewModel = ProfileViewModel(userRepository, bookRepository)
    }

    @Test
    fun `loadProfile combines user and stats data successfully`() = runTest {
        // Given
        coEvery { userRepository.getMe() } returns mockUser
        coEvery { bookRepository.getProfileStats() } returns mockStats

        // When
        viewModel.loadProfile()
        advanceUntilIdle()

        // Then
        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals("testuser", state.username)
        assertEquals("Old Bio", state.bio)
        assertEquals(10, state.booksRead)
        assertEquals(2, state.currentlyReading)
        assertEquals(5, state.wantToRead)
    }

    @Test
    fun `loadProfile handles errors`() = runTest {
        // Given
        coEvery { userRepository.getMe() } throws RuntimeException("API Error")

        // When
        viewModel.loadProfile()
        advanceUntilIdle()

        // Then
        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals("API Error", state.error)
    }

    @Test
    fun `updateBio updates local state immediately`() {
        // This is synchronous, no need for runTest or coroutines
        viewModel.updateBio("New Draft Bio")

        assertEquals("New Draft Bio", viewModel.uiState.value.bio)
    }

    @Test
    fun `saveProfile calls repository and updates state`() = runTest {
        // Given
        val updatedUser = mockUser.copy(bio = "Saved Bio")

        // Pre-set the state to have a username (needed for the API call logic)
        // We simulate a load first so the VM has the username
        coEvery { userRepository.getMe() } returns mockUser
        coEvery { bookRepository.getProfileStats() } returns mockStats
        viewModel.loadProfile()
        advanceUntilIdle()

        // Now user changes bio
        viewModel.updateBio("Saved Bio")

        // Mock the update call
        coEvery { userRepository.updateProfile(any(), any()) } returns updatedUser

        // When
        viewModel.saveProfile()
        advanceUntilIdle()

        // Then
        val state = viewModel.uiState.value
        assertEquals("Saved Bio", state.bio)
        assertEquals(null, state.error)
    }

    @Test
    fun `uploadAvatar calls repository`() = runTest {
        // Given
        val mockPart = mockk<MultipartBody.Part>()
        val updatedUser = mockUser.copy(profilePictureUrl = "http://new.jpg")

        coEvery { userRepository.uploadAvatar(mockPart) } returns updatedUser

        // When
        viewModel.uploadAvatar(mockPart)
        advanceUntilIdle()

        // Then
        val state = viewModel.uiState.value
        assertEquals("http://new.jpg", state.profilePictureUrl)
        assertEquals(null, state.error)
    }
}