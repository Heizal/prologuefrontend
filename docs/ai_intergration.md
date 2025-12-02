# AI Integration — Prologue Android Frontend
This document explains how the Android application integrates with the Prologue backend’s AI services.
While the LLM inference and orchestration are handled server-side (Gemma 3 via Ollama), the frontend is responsible for:
- sending prompts 
- receiving structured AI responses 
- displaying conversational messages 
- storing chat previews & details locally 
- enabling rediscovery and home recommendations

## AI Features in the app
The frontend integrates with the backend’s AI module through three major features:

**1. Discover Chat (Main AI Chat Interface)**
Users type a message → backend processes it using the LLM → returns:
- an assistant message 
- structured book recommendations 
- a stored chat ID 
- metadata for UI display

**2. Home AI Pick**
Daily, the app fetches an AI-generated recommendation:
- short message 
- suggested book 
- optional cover image

Displayed at the top of the Home screen.

**3. Rediscover (Future Iteration)**
_At the moment this feature works with the books already in the users library_

AI will analyze reading history and suggests:
- a book from the user’s library 
- a rediscovery message 
- why the user might enjoy revisiting it

## Endpoints used
The frontend communicates with these backend AI endpoints:

| Feature       | Method | Endpoint                            |
| ------------- | ------ | ----------------------------------- |
| Discover Chat | POST   | `/api/recommendations`              |
| Home Pick     | GET    | `/api/recommendations/home?userId=` |
| Rediscover    | GET    | `/api/books/rediscover?userId=`     |
| Chat History  | GET    | `/api/chats`                        |
| Chat Detail   | GET    | `/api/chats/{chatId}`               |

## Sending prompts to the LLM 
When the user enters a prompt, the Android app constructs:
````json
{
  "prompt": "Give me dark academia recommendations"
}
````

Using Retrofit:
````kotlin
suspend fun recommend(request: AIPickRequest): AIPickResponse
````
The backend handles:

- LLM model selection 
- safety filtering 
- latency tracking 
- storing the conversation

The app only renders structured output.

## Receiving AI Responses
A typical `AIPickResponse` includes:
````json
{
  "assistantMessage": "Here are some dark academia books you’ll love...",
  "books": [
    { "title": "...", "author": "..."}
  ],
  "chatId": "uuid-123",
  "timestamp": "2025-11-09T14:37:00Z"
}
````
The app:

- Appends user message to UI 
- Appends assistant message 
- Displays recommended books 
- Saves chat preview to local state 
- Navigates to full chat if needed

## Chat History & Detail View
The app uses:
````bash
GET /api/chats           → list of chat previews
GET /api/chats/{id}      → full conversation thread
````

The UI renders:

- user vs AI messages 
- recommended books 
- previous prompts

This allows users to revisit older recommendations.

## Home AI Pick Integration
The Home screen call:
````bash
GET /api/recommendations/home?userId=123
````
This returns:

- daily AI recommendation 
- short explanation 
- optionally book details

Rendered in a AI Picks For You card.

## AI Error Handling in the app
The app gracefully handles:

- **Slow AI responses** → shows loading state 
- LLM timeouts → toast/snackbar 
- Missing recommendations → fallback UI
- Empty response → friendly AI message 
- HTTP 500 → “AI unavailable, try again later”

This ensures smooth UX even when the model is unavailable.

## Security & Safety
The frontend relies on backend-level safety:

- No prompts go directly to the LLM 
- The backend applies safety filters 
- User ID must match the JWT token 
- Each conversation is stored for auditability

Frontend does not store or process prompts locally beyond UI state.

## Related Docs
- `api_integration.md `
- `architecture_overview.md `
- `project_setup.md`
