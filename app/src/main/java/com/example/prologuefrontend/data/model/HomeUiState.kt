package com.example.prologuefrontend.data.model

data class HomeUiState(
    val aiPick: HomePickUiState = HomePickUiState(),
    val rediscover: RediscoverUiState = RediscoverUiState(),
    val activity: RecentActivityUiState = RecentActivityUiState()
)

data class HomePickUiState(
    val title: String? = null,
    val author: String? = null,
    val thumbnailUrl: String? = null,
    val message: String? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

data class RediscoverUiState(
    val title: String? = null,
    val author: String? = null,
    val thumbnailUrl: String? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

data class RecentActivityUiState(
    val lastBookTitle: String? = null,
    val lastBookAuthor: String? = null,
    val lastAIPick: String? = null,
    val lastAIPickAuthor: String? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)
