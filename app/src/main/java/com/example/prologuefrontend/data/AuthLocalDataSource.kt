package com.example.prologuefrontend.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.*

class AuthLocalDataSource(private val context: Context) {

    fun getToken(): Flow<String?> =
        context.authDataStore.data.map { it[AuthPrefs.token] }

    fun getUserId(): Flow<Long?> =
        context.authDataStore.data.map { it[AuthPrefs.userId] }

    suspend fun saveAuth(token: String, userId: Long) {
        context.authDataStore.edit {
            it[AuthPrefs.token] = token
            it[AuthPrefs.userId] = userId
        }
    }

    suspend fun clear() {
        context.authDataStore.edit { it.clear() }
    }
}