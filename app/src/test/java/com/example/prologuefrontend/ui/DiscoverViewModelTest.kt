package com.example.prologuefrontend.ui

import com.example.prologuefrontend.data.model.ChatPreview
import com.example.prologuefrontend.data.model.RecommendationBookDto
import com.example.prologuefrontend.data.model.RecommendationResponse
import com.example.prologuefrontend.data.model.ScreenState
import com.example.prologuefrontend.data.repository.ChatRepository
import com.example.prologuefrontend.data.repository.DiscoverRepository
import com.example.prologuefrontend.ui.viewmodels.DiscoverViewModel
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class DiscoverViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var repo: DiscoverRepository
    private lateinit var chatRepo: ChatRepository
    private lateinit var vm: DiscoverViewModel

    @Before
    fun setup() {
        repo = mockk()
        chatRepo = mockk()
        vm = DiscoverViewModel(repo, chatRepo)
    }

    @Test
    fun `loadChatPreviews loads previews successfully`() = runTest {
        val previews = listOf(ChatPreview("1", "Hello", "2025-01-01", "url.png"))
        coEvery { chatRepo.getChatPreviews() } returns previews

        vm.loadChatPreviews()
        advanceUntilIdle()

        val state = vm.state.value
        assertEquals(previews, state.sidebar.previews)
        assertEquals(false, state.sidebar.isLoading)
        assertEquals(null, state.sidebar.error)
    }

    @Test
    fun `loadChatPreviews sets error on failure`() = runTest {
        coEvery { chatRepo.getChatPreviews() } throws RuntimeException("network fail")

        vm.loadChatPreviews()
        advanceUntilIdle()

        val state = vm.state.value
        assertEquals("network fail", state.sidebar.error)
        assertEquals(false, state.sidebar.isLoading)
    }

    @Test
    fun `sendUserMessage emits Recommendations on success`() = runTest {
        val books = listOf(RecommendationBookDto("1", "T", "A", "img", "desc"))
        val response = RecommendationResponse("hello!", recommendations = books)

        coEvery { repo.fetchRecommendations("hey") } returns response

        vm.sendUserMessage("hey")
        advanceUntilIdle()

        val screen = vm.state.value.screen
        assertTrue("Expected Recommendations state but got $screen", screen is ScreenState.Recommendations)

        val rec = screen as ScreenState.Recommendations
        assertEquals("hello!", rec.responseMessage)
        assertEquals(books, rec.books)
    }

    @Test
    fun `sendUserMessage emits Error state on failure`() = runTest {
        coEvery { repo.fetchRecommendations(any()) } throws RuntimeException("boom")

        vm.sendUserMessage("aaa")
        advanceUntilIdle()

        val screen = vm.state.value.screen
        assertTrue(screen is ScreenState.Error)
        assertEquals("boom", (screen as ScreenState.Error).message)
    }

    @Test
    fun `addBook optimistic update then success`() = runTest {
        val book = RecommendationBookDto("1", "A", "B", "img", "desc")
        val response = RecommendationResponse("msg", recommendations = listOf(book))

        coEvery { repo.fetchRecommendations(any()) } returns response
        coJustRun { repo.addBookToLibrary(any()) }


        vm.sendUserMessage("test")
        advanceUntilIdle()

        vm.addBook(book)
        advanceUntilIdle()

        val screen = vm.state.value.screen
        assertTrue(screen is ScreenState.Recommendations)

        val recState = screen as ScreenState.Recommendations
        assertTrue(recState.inLibrary.contains("1"))

        coVerify { repo.addBookToLibrary(any()) }
    }

    @Test
    fun `addBook reverts optimistic update on failure`() = runTest {
        val book = RecommendationBookDto("1", "A", "B", "img", "desc")
        val response = RecommendationResponse("msg", recommendations = listOf(book))

        coEvery { repo.fetchRecommendations(any()) } returns response
        coEvery { repo.addBookToLibrary(any()) } throws RuntimeException("fail")

        vm.sendUserMessage("test")
        advanceUntilIdle()

        vm.addBook(book)
        advanceUntilIdle()

        val screen = vm.state.value.screen as ScreenState.Recommendations
        assertTrue(!screen.inLibrary.contains("1"))
    }


    @Test
    fun `askAgain uses last prompt and gets new recommendations`() = runTest {
        val r1 = RecommendationResponse("first", recommendations = emptyList())
        val r2 = RecommendationResponse("second", recommendations = emptyList())

        coEvery { repo.fetchRecommendations(any()) } returns r1 andThen r2

        vm.sendUserMessage("hey")
        advanceUntilIdle()

        vm.askAgain()
        advanceUntilIdle()

        val screen = vm.state.value.screen as ScreenState.Recommendations
        assertEquals("second", screen.responseMessage)
    }

    @Test
    fun `askAgain with no last prompt returns Initial`() = runTest {
        vm.askAgain()
        assertTrue(vm.state.value.screen is ScreenState.Initial)
    }

    @Test
    fun `startNewChat clears conversation but keeps previews`() = runTest {
        val previews = listOf(ChatPreview("1", "x", "y", null))

        coEvery { chatRepo.getChatPreviews() } returns previews

        vm.loadChatPreviews()
        advanceUntilIdle()

        vm.startNewChat()
        advanceUntilIdle()

        val state = vm.state.value
        assertTrue(state.screen is ScreenState.Initial)
        assertEquals(previews, state.sidebar.previews)
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
class MainDispatcherRule(
    private val testDispatcher: TestDispatcher = UnconfinedTestDispatcher()
) : TestWatcher() {
    override fun starting(description: Description) {
        Dispatchers.setMain(testDispatcher)
    }

    override fun finished(description: Description) {
        Dispatchers.resetMain()
    }
}
