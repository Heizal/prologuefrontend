# Prologue – AI-Powered Reading Assistant

## Project Overview
Prologue is a modern Android application designed to be your personal AI-powered reading companion. Built entirely with **Kotlin** and **Jetpack Compose**, the app integrates Large Language Model (LLM) capabilities to provide personalized book recommendations, intelligent "rediscovery" of past reads, and an interactive chat interface for literary exploration. It communicates with a Spring Boot backend to manage user data and leverage AI endpoints, bridging the gap between traditional e-reading tools and modern AI assistance.

## Key Features
*   **My Books Library:** Seamlessly upload book files, organize your collection, and update reading states (Currently Reading, Want to Read, Read).
*   **Discover Mode:** Engage in an AI-driven chat interface to get tailored book recommendations based on your mood or interests.
*   **Rediscover:** Intelligent resurfacing of books from your past reading history to spark nostalgia or re-reading.
*   **Home Dashboard:** Features a dynamic "AI Pick of the Day" and a visual summary of your current reading activity.
*   **Chat History:** Access and review previous AI conversations with a detailed history view.
*   **User Profile:** Manage user details and upload custom profile pictures.
*   **Secure Authentication:** Full login and registration system secured by backend JWT (JSON Web Tokens).

## Tech Stack

### Frontend (Android)
*   **Language:** Kotlin
*   **UI Framework:** Jetpack Compose (Material 3)
*   **Architecture:** MVVM (Model-View-ViewModel)
*   **Dependency Injection:** Hilt
*   **Networking:** Retrofit + OkHttp
*   **Image Loading:** Coil
*   **Local Storage:** Room Database
*   **Navigation:** Jetpack Navigation Compose
*   **Concurrency:** Kotlin Coroutines & Flow

### Backend
*   **Framework:** Spring Boot (Java)
*   [Link to Backend Repository](https://github.com/Heizal/prologue-backend)

## 🏗 Architecture Overview
Prologue follows a clean **MVVM architecture** with the Repository pattern to ensure separation of concerns and testability. The app utilizes a "Single Source of Truth" principle by mediating data between the remote backend and the local Room database.

**Data Flow:**
````
UI (Jetpack Compose)
        ↓
ViewModel (StateFlow, UI State Management)
        ↓
Repository Layer
        ↓
Network Module (Retrofit) / Local DB (Room)
        ↓
Spring Boot Backend API

````

## Project Structure
````bash
/ui
   /screens
   /components
   /theme
   /viewmodels

/data
   /local (Room)
   /model
   /remote
   /repository
   
/network
  /AuthDtos.kt

/navigation
  /AuthGraph.kt
  /MainGraph.kt
  /RootNavGraph.kt
  
MainActivity
PrologueApp
````

## Running the App

**Prerequisites**
- Android Studio
- Locally running backend
- Update base URL in `NetworkModule.kt `

**Steps**
- Clone the repository 
- Open in Android Studio 
- Sync Gradle 
- Connect a device or launch an emulator 
- Run the application 
- Log in or register -> access rest of backend endpoints

## Backend dependency

This app communicates with the Prologue backend service:

[Backend Repository](https://github.com/Heizal/prologue-backend)

**You must run the backend in order to interact with the app**

## UI Preview
[Screen recording here]()

## Further Documentation

All additional details are located in the **docs/** folder:

- [project_setup.md](https://github.com/Heizal/prologuefrontend/blob/main/docs/project_setup.md)
- [architecture_overview.md](https://github.com/Heizal/prologuefrontend/blob/main/docs/architecture_overview.md)
- [api_integration.md](https://github.com/Heizal/prologuefrontend/blob/main/docs/api_integration.md) 
- [ai_integration.md](https://github.com/Heizal/prologuefrontend/blob/main/docs/project_setup.md)

## Credits
Developed by **Patricia Heizal Nagginda** as part of the Capstone Project at
**CODE University of Applied Sciences.**

