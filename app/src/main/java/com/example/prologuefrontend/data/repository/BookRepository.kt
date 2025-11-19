package com.example.prologuefrontend.data.repository

import android.content.Context
import android.net.Uri
import androidx.annotation.UiContext
import com.example.prologuefrontend.data.model.Book
import com.example.prologuefrontend.data.remote.ApiService
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import jakarta.inject.Singleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

@Singleton
class BookRepository  @Inject constructor(
    private val api: ApiService,
    @ApplicationContext private val appContext: Context
) {
    suspend fun getBooks(query: String? = null): List<Book> {
        return api.getBooks(query)
    }

    suspend fun deleteBook(id: Long) {
        api.deleteBook(id)
    }

    suspend fun uploadBook(fileUri: Uri, cacheDir: File): Book = withContext(Dispatchers.IO) {
        val context = appContext
        val inputStream = context.contentResolver.openInputStream(fileUri)
            ?: throw IllegalArgumentException("Cannot open input stream for URI: $fileUri")

        val tempFile = File(cacheDir, "upload_${System.currentTimeMillis()}.pdf")
        tempFile.outputStream().use { output ->
            inputStream.copyTo(output)
        }

        val requestFile = tempFile.asRequestBody("multipart/form-data".toMediaTypeOrNull())
        val body = MultipartBody.Part.createFormData("file", tempFile.name, requestFile)
        api.uploadBook(body)
    }
}