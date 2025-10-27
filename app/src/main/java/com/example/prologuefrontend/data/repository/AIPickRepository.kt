package com.example.prologuefrontend.data.repository

import com.example.prologuefrontend.data.model.AIPick
import com.example.prologuefrontend.data.remote.RetrofitInstance

class AIPickRepository {
    suspend fun getAIPick(): AIPick {
        return RetrofitInstance.api.getAIPick()
    }
}