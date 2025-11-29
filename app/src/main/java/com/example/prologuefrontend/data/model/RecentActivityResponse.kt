package com.example.prologuefrontend.data.model

data class RecentActivityResponse(
    val latestBook: RecentBookResponse?,
    val latestRecommendation: RecentRecResponse?
)

data class RecentBookResponse(
    val title: String?,
    val author: String?,
    val thumbnailUrl: String?,
    val infoLink: String?,
)

data class RecentRecResponse(
    val title: String?,
    val author: String?,
    val thumbnailUrl: String?,
    val infoLink: String?,
    val reason: String?
)