package com.example.prologuefrontend.data.remote

import com.example.prologuefrontend.data.model.Book
import com.example.prologuefrontend.data.model.ChatDetail
import com.example.prologuefrontend.data.model.ChatPreview
import com.example.prologuefrontend.data.model.HomePickResponse
import com.example.prologuefrontend.data.model.RecentActivityResponse
import com.example.prologuefrontend.data.model.RecommendationRequest
import com.example.prologuefrontend.data.model.RecommendationResponse
import com.example.prologuefrontend.data.model.RediscoverResponse
import com.example.prologuefrontend.data.model.UpdateProfileRequest
import com.example.prologuefrontend.data.model.UserResponse
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
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

    @GET("users/me")
    suspend fun getMe(): UserResponse

    @PUT("users/me")
    suspend fun updateProfile(
        @Body body: UpdateProfileRequest
    ): UserResponse

    @Multipart
    @POST("users/me/avatar")
    suspend fun uploadAvatar(
        @Part file: MultipartBody.Part
    ): UserResponse

    @GET("/api/recommendations/home")
    suspend fun getHomePick(
        @Query("userId") userId: Long
    ): HomePickResponse

    @GET("/api/books/rediscover")
    suspend fun rediscover(
        @Query("userId") userId: Long
    ): RediscoverResponse

    @GET("/api/activity/recent")
    suspend fun getRecentActivity(
        @Query("userId") userId: Long
    ): RecentActivityResponse

    @GET("api/chats")
    suspend fun getChatPreviews(): List<ChatPreview>

    @GET("api/chats/{chatId}")
    suspend fun getChatDetail(
        @Path("chatId") chatId: String
    ): ChatDetail


}