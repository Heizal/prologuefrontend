# Project Setup - Prologue Android Frontend

This document describes how to install, configure, and run the Prologue Android application. It includes prerequisites, environment configuration, backend dependencies, build information, and troubleshooting guidance.

## Prerequisites

Before running the project, ensure the following tools are installed:

**Required**

- Android Studio Ladybug (or newer)

- JDK 17+ (bundled in Android Studio)

- Gradle (handled automatically by Android Studio)

- Android SDK with:
  - API Level 34 (Android 14)
  - Build Tools 34.0.x

**Backend Requirements**

The frontend requires the **Prologue Spring Boot backend** to run.

You may:

- Run it locally `(localhost:8080)`

## Cloning the project

````bash
git clone https://github.com/Heizal/prologuefrontend
cd prologuefrontend

````
Open the project in Android Studio

## Environment Configuration

Update the base API URL inside:
````bash
data/remote/NetworkModule.kt

````

## Build and Run

**To run the app**

- Open Android Studio 
- Click Sync Gradle 
- Select a device or emulator 
- Press Run ▶

**First Launch**

- You will be prompted to Log In or Register 
- Authentication is handled via JWT tokens from the backend

## Authentication and Tokens

The frontend communicates with the backend using:

- Bearer JWT tokens (stored in-memory)
- Secure API calls through Retrofit interceptors 
- Automatic token injection for protected endpoints

## File Upload Configuration

For book uploads (cover images) and profile image uploads:

- The backend must allow:
  - Multipart file uploads 
  - CORS for app domain (default open in your backend)

On the Android side, this works through:
````kotlin
@Multipart
@POST("books/upload")
suspend fun uploadBook(@Part file: MultipartBody.Part): BookDto

````

## Troubleshooting

**❗ Backend connection fails on emulator**

Use **10.0.2.2** instead of localhost.

**❗ Unauthorized (401)**

- Ensure backend is running 
- Log in again to refresh JWT 
- Check if backend DB has your user stored

**❗ Image upload fails**

- Backend upload directory must exist 
- Your backend file permissions must allow writing 
- Ensure the backend URL in the app is correct

**❗ App will not compile**

- Click File → Invalidate Caches & Restart 
- Ensure you installed Android SDK 34

## Maintained by

**Patricia Heizal Nagginda**
Capstone Project @ CODE University of Applied Sciences