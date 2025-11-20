package com.example.prologuefrontend.data.repository

import com.example.prologuefrontend.data.model.ChatDetail
import com.example.prologuefrontend.data.model.ChatPreview
import com.example.prologuefrontend.data.remote.ApiService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChatRepository @Inject constructor(
    private val api: ApiService
) {
    suspend fun getChatPreviews(): List<ChatPreview> {
        return api.getChatPreviews()

    }

    suspend fun getChatDetail(chatId: String): ChatDetail {
        return api.getChatDetail(chatId)
    }
}