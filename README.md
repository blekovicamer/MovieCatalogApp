🎬 MovieCatalogApp

A modern, high-performance Android application designed for movie enthusiasts. MovieCatalogApp allows users to browse popular movies, view detailed information, watch trailers natively, and manage their personal favorites—all wrapped in a premium dark-themed UI.
🚀 Features

    Real-time Movie Discovery: Fetches the latest popular movies and trending actors using the TMDB (The Movie Database) API.

    Comprehensive Details: Deep-dive into movie overviews, production budgets, revenue, and country of origin.

    Native Trailer Playback: Integrated YouTube Player Data API for high-quality, secure trailer streaming without leaving the app.

    Cast & Crew: Horizontal scrolling cast lists showing top-billed actors and their roles.

    Personalization: * Favorites System: Save movies to a local "Must Watch" list using SharedPreferences.

        User Ratings: Interactive star-rating system to keep track of your personal reviews.

    Modern UI: Responsive design using CoordinatorLayout, NestedScrollView, and CardView for a layered, professional feel.

🛠 Tech Stack

    Language: Java / Kotlin (Android)

    Networking: Retrofit 2 for REST API communication.

    Image Loading: Glide for smooth image caching and backdrop rendering.

    Video Playback: Android-YouTube-Player for native YouTube integration.

    UI Components: Material Design, RecyclerView, ConstraintLayout.

📸 Screenshots
Home Screen	Movie Details
	
⚙️ Setup & Installation

    Clone the repository:
    Bash

    git clone https://github.com/yourusername/MovieCatalogApp.git

    Get an API Key: Sign up at The Movie Database (TMDB) to get your free API key.

    Add your Key: Open RetrofitClient.java and replace the API_KEY constant with your own.

    Build & Run: Open the project in Android Studio and run it on an emulator or physical device.
