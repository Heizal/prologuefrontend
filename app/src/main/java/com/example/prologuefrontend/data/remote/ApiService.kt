package com.example.prologuefrontend.data.remote

import com.example.prologuefrontend.data.model.AddBookRequest
import com.example.prologuefrontend.data.model.AddBookResponse
import com.example.prologuefrontend.data.model.Book
import com.example.prologuefrontend.data.model.RecommendationRequest
import com.example.prologuefrontend.data.model.RecommendationResponse
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @POST("api/recommendations")
    suspend fun getRecommendations(
        @Body request: RecommendationRequest
    ): RecommendationResponse

    @GET("books")
    suspend fun getBooks(@Query("q") query: String? = null): List<Book>

    @Multipart
    @POST("books/upload")
    suspend fun uploadBook(@Part file: MultipartBody.Part): Book

    @POST("books/add")
    suspend fun addBook(@Body book: Book): Book

    @PUT("books/{id}")
    suspend fun updateBook(@Path("id") id: Long, @Body book: Book): Book

    @DELETE("books/{id}")
    suspend fun deleteBook(@Path("id") id: Long)
}