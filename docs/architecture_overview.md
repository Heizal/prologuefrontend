# Architecture Overview — Prologue Android Frontend
This document provides a detailed overview of the architecture used in the Prologue Android application. It describes the structural decisions, data flow patterns, navigation model, dependency injection, storage layers, and how the app integrates with the backend API and AI services.

## Architecture Style
Prologue follows a modern MVVM (Model–View–ViewModel) architecture, combined with:

- Unidirectional Data Flow (UDF)
- Jetpack Compose UI 
- StateFlow for UI state 
- Repository pattern 
- Hilt for dependency injection  
- Retrofit for networking

This ensures that:

- UI is reactive and declarative 
- Business logic is isolated 
- Network and database layers are abstracted 
- Side effects are confined to viewmodels

## Layered Architecture
The app is organized into three main layers:
````bash
ui/               (UI + ViewModels)
navigation/       (Navigation)
data/             (Models, Network, Repository, Local Storage)
````
**🟦 2.1 Ui Layer (UI)**
Implemented using Jetpack Compose.
**Responsibilities:**

- Display state from ViewModels 
- Handle user events 
- Trigger ViewModel actions 
- Navigate between screens

UI does not perform business logic or data fetching.

**🟧 2.2 Navigation Layer**
Handles Auth navigation and navigation between screens

**🟥 2.3 Data Layer**
Contains:
- Data models and mappers
- Repositories (core abstraction layer)
- Retrofit API interfaces

**Responsibilities:**

- Fetch data from backend 
- Cache data locally 
- Provide unified data interface to ViewModels

## Data Flow
The app implements an undirected data flow
````
User Action
    ↓
ViewModel (logic, state updates)
    ↓
Repository (data source selection)
    ↓
Network (Retrofit)
    ↓
Repository returns model
    ↓
ViewModel updates UI state
    ↓
Compose UI re-renders automatically
````
This ensures predictable and debuggable behavior across all features.

## Networking Architecture
**- Retrofit** -> Used for all backend communication with custom interfaces
**- OkHttp Interceptor** -> Automatically injects JWT tokens
**- Error Handling**: All network calls use: `try/catch` mechanism and `Result<T>` wrappers

## State Management
The app uses:

- `StateFlow` for UI state streams
- `MutableStateFlow `inside ViewModels 
- `Immutable UiState` data classes

Example pattern:
````kotlin
data class DiscoverUiState(
    val messages: List<Message> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
````

ViewModel:
````kotlin
val uiState: StateFlow<DiscoverUiState> = _uiState.asStateFlow()
````

UI subscribes:
````kotlin
val state by viewModel.uiState.collectAsState()
````
This produces:
- replayable state 
- lifecycle-aware updates 
- no memory leaks 
- smooth animations in Compose

## Navigation Architecture
The app uses Jetpack Navigation Compose with a Nested Graph strategy. This separates the application into logical flows **(Authentication vs. Main Content)**, ensuring a modular structure where the Bottom Navigation Bar only appears when the user is authenticated.

**Hierarchy**: The logic is split into three distinct layers:

- 1.Root Graph `(RootNavGraph.kt)`: The entry point that hosts the NavHost and switches between the auth and main nested graphs.
- 2.Auth Graph `(AuthGraph.kt)`: Encapsulates onboarding screens **(Login, Register)**. No Bottom Bar present.
- 3.Main Graph `(MainGraph.kt)`: Encapsulates the authenticated user experience. It utilizes a wrapper composable (`MainScreenContainer`) to inject the Scaffold and BottomNavBar for top-level screens **(Home, My Books, Discover, Profile)**.

## Dependency Injection (Hilt)
Hilt provides:

- Retrofit instance 
- OkHttp client
- Repository implementations 
- ViewModel creation

**Example**
````kotlin
@HiltViewModel
class DiscoverViewModel @Inject constructor(
    private val repository: RecommendationRepository
) : ViewModel()
````
Benefits:
- Testability
- Separation of concerns 
- Centralized configuration

## Data flow diagrams

### (1) Unidirectional Data Flow
````mermaid
sequenceDiagram
    participant U as User
    participant UI as Compose UI
    participant VM as ViewModel
    participant REP as Repository
    participant NET as Retrofit API
    participant BE as Backend

    U->>UI: User interacts with screen
    UI->>VM: UI events (clicks, input)
    VM->>REP: Need data or action
    REP->>NET: API call
    NET->>BE: HTTP request
    BE-->>NET: JSON response
    NET-->>REP: Parsed DTO
    REP-->>VM: Domain models
    VM-->>UI: Update StateFlow
    UI->>UI: Recompose UI
````

### (2) AI Chat Flow
````mermaid
sequenceDiagram
    participant User as User
    participant UI as Discover Screen
    participant VM as DiscoverViewModel
    participant Repo as RecommendationRepository
    participant API as /api/recommendations
    participant LLM as LLM Engine
    participant Hist as History Service

    User->>UI: Types prompt
    UI->>VM: onPromptEntered(prompt)
    VM->>UI: Show loading state
    VM->>Repo: sendPrompt()
    Repo->>API: POST /api/recommendations
    API->>LLM: Generate recommendation
    LLM-->>API: AI response + books
    API->>Hist: Save chat + metadata
    API-->>Repo: AIPickResponse
    Repo-->>VM: Return mapped data
    VM-->>UI: Update messages + books
    UI->>UI: Render chat thread
````

### (3) Book Upload Flow
````mermaid
sequenceDiagram
    participant User as User
    participant UI as Upload Screen
    participant VM as BookViewModel
    participant Repo as BookRepository
    participant API as /books/upload
    participant Storage as Backend File Storage

    User->>UI: Select book from local storage
    UI->>VM: uploadBook(file)
    VM->>Repo: Build MultipartBody.Part
    Repo->>API: POST /books/upload
    API->>Storage: Save file to uploads/
    Storage-->>API: File saved metadata
    API-->>Repo: BookDto
    Repo-->>VM: Return new book
    VM-->>UI: Update UI with new book
````

## Future Improvements
- Support Push notifications
- Introduce Offline Caching with Room
- Modularize the Codebase Using Multi-Module Gradle
- Implement Full Error & Retry Handling
- Move UI State to a Shared MVI Framework
- Improve Recommendation UI Parsing

