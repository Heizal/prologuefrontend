package com.example.prologuefrontend.ui

import com.example.prologuefrontend.data.repository.AuthRepository
import com.example.prologuefrontend.network.auth.dto.AuthResponse
import com.example.prologuefrontend.ui.viewmodels.AuthState
import com.example.prologuefrontend.ui.viewmodels.AuthViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.testng.annotations.AfterTest
import org.testng.annotations.BeforeTest

@OptIn(ExperimentalCoroutinesApi::class)
class AuthViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private val repository: AuthRepository = mockk()
    private lateinit var viewModel: AuthViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = AuthViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is Idle`() = runTest {
        assertEquals(AuthState.Idle, viewModel.authState.value)
    }

    @Test
    fun `login success sets state to Loading then Success`() = runTest {
        // Arrange
        coEvery { repository.login("mail@mail.com", "pass") } returns AuthResponse(
            token = "token",
            userId = 1L
        )

        // Act
        viewModel.login("mail@mail.com", "pass")

        // Let coroutines run
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        assertEquals(AuthState.Success, viewModel.authState.value)
        coVerify(exactly = 1) { repository.login("mail@mail.com", "pass") }
    }

    @Test
    fun `login failure sets state to Error`() = runTest {
        // Arrange
        coEvery { repository.login(any(), any()) } throws RuntimeException("Login failed")

        // Act
        viewModel.login("wrong@mail.com", "badpass")

        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        val state = viewModel.authState.value
        assert(state is AuthState.Error)
        assertEquals("Login failed", (state as AuthState.Error).message)
    }

    @Test
    fun `signup success sets state to Success`() = runTest {
        // Arrange
        coEvery {
            repository.signup("mail@mail.com", "pass", "user")
        } returns AuthResponse(
            token = "token",
            userId = 1L
        )

        // Act
        viewModel.signup("mail@mail.com", "pass", "user")

        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        assertEquals(AuthState.Success, viewModel.authState.value)
        coVerify(exactly = 1) {
            repository.signup("mail@mail.com", "pass", "user")
        }
    }

    @Test
    fun `signup failure sets state to Error`() = runTest {
        // Arrange
        coEvery { repository.signup(any(), any(), any()) } throws RuntimeException("Signup failed")

        // Act
        viewModel.signup("mail@mail.com", "pass", "user")

        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        val state = viewModel.authState.value
        assert(state is AuthState.Error)
        assertEquals("Signup failed", (state as AuthState.Error).message)
    }

    @Test
    fun `reset sets state back to Idle`() = runTest {
        // Put it in some non-idle state
        coEvery { repository.login(any(), any()) } returns AuthResponse("token", 1L)

        viewModel.login("mail@mail.com", "pass")
        testDispatcher.scheduler.advanceUntilIdle()
        assertEquals(AuthState.Success, viewModel.authState.value)

        // Act
        viewModel.reset()

        // Assert
        assertEquals(AuthState.Idle, viewModel.authState.value)
    }

    @Test
    fun `logout calls repository and sets state to Idle`() = runTest {
        // Arrange
        coEvery { repository.logout() } returns Unit

        // Put the state into something else
        viewModel.reset()
        assertEquals(AuthState.Idle, viewModel.authState.value)

        // Act
        viewModel.logout()
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        coVerify(exactly = 1) { repository.logout() }
        assertEquals(AuthState.Idle, viewModel.authState.value)
    }
}