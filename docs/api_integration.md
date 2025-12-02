# API Integration — Prologue Android Frontend
This document describes how the Android application communicates with the Prologue backend. It explains endpoint usage, Retrofit integration, authentication, file uploads, error handling, and how different features map to backend APIs.

The frontend communicates with a **Spring Boot backend** that exposes REST endpoints for authentication, books, recommendations, rediscovery, history, and profile updates.

## Retrofit Setup
All HTTP communication is handled through **Retrofit + OkHttp**.

**Base Configuration**

`NetworkModule.kt` provides the global Retrofit client:
````kotlin
    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080/") // Add your actual baseUrl 
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
````
**Authentication Interceptor**

JWT tokens are added to each request automatically through the **AuthInterceptor.kt**

## Authentication Endpoints
Used for login and registration.

| Action   | Method | Endpoint         |
| -------- | ------ | ---------------- |
| Register | POST   | `/auth/register` |
| Login    | POST   | `/auth/login`    |

After login, the app stores:

- JWT Token
- User ID 
- Username

## Books Module
Handles uploading, adding, updating, and retrieving books.

**Endpoints Used**

| Action                  | Method | Endpoint        |
| ----------------------- | ------ | --------------- |
| Add new book            | POST   | `/books`        |
| Get all user books      | GET    | `/books`        |
| Search books            | GET    | `/books?q=`     |
| Get one book            | GET    | `/books/{id}`   |
| Update book             | PUT    | `/books/{id}`   |
| Delete book             | DELETE | `/books/{id}`   |
| Add to library          | POST   | `/books/add`    |
| Upload book (multipart) | POST   | `/books/upload` |

**Multipart Upload Example**
````kotlin
    @Multipart
    @POST("books/upload")
    suspend fun uploadBook(@Part file: MultipartBody.Part): Book
````

## AI Recommendations (Discover)
Initiates a conversation with the LLM via the backend AI orchestration module.

**Endpoint**

| Action                | Method | Endpoint               |
| --------------------- | ------ | ---------------------- |
| Get AI recommendation | POST   | `/api/recommendations` |

**Request Payload**
````json
{
  "prompt": "Give me fantasy romance books"
}
````
**Response Contains**

- Assistant message 
- List of recommended books 
- Generated metadata 
- Stored chat ID

## Home AI Pick
Fetches daily AI-generated recommendation for the Home screen.

**Endpoint**

| Action    | Method | Endpoint                                |
| --------- | ------ | --------------------------------------- |
| Home pick | GET    | `/api/recommendations/home?userId={id}` |

## Rediscover
Retrieves an AI-generated “Rediscover your library” suggestion.

**Endpoint**

| Action     | Method | Endpoint                            |
| ---------- | ------ | ----------------------------------- |
| Rediscover | GET    | `/api/books/rediscover?userId={id}` |

**Response includes:**

- Rediscover message 
- Recommended book 
- Reasoning

## Chat History Module
Used to fetch entire conversation history and individual chat threads.

**Endpoints**

| Action                | Method | Endpoint              |
| --------------------- | ------ | --------------------- |
| Get all chat previews | GET    | `/api/chats`          |
| Get chat detail       | GET    | `/api/chats/{chatId}` |

## Recent Activity
Used to show user’s last interactions or recent reading activity.

**Endpoint**

| Action                       | Method | Endpoint                           |
| ---------------------------- | ------ | ---------------------------------- |
| Get recent user interactions | GET    | `/api/activity/recent?userId={id}` |

## Profile Module
Gets user details and uploads profile image.

**Endpoints**

| Action               | Method | Endpoint           |
| -------------------- | ------ |--------------------|
| Upload profile image | POST   | `/users/me/avatar` |
| Get user details     | GET    | `/users/me`        |
| Update fields        | PUT    | `/users/me`        |

## Error Handling
The app handles:

**Common errors**

- `401 Unauthorized` → token expired or missing 
- `403 Forbidden` → user mismatch 
- `404 Not Found` → book or chat missing 
- `500 Server Error` → AI model failure or backend crash

**Android-side responses**

- Snackbar UI for user-facing errors 
- Safe API wrappers using `Result<T>` or `try/catch`

## Related Documents

- architecture_overview.md 
- ai_integration.md 
- project_setup.md



