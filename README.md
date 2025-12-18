# 🎵 Beatify

---

## 📱 Demo Video & Screenshots

<div align="center">

<table>
<tr>
<td align="center" width="50%">
  
**🎥 Demo Video**

</td>
<td align="center" width="50%">
  
**🔒 Lock Screen**

</td>
</tr>
<tr>
<td width="50%" align="center">

https://github.com/user-attachments/assets/3987d6e6-1941-492d-8518-33982e24fad4

</td>
<td width="50%" align="center">

<img src="https://github.com/user-attachments/assets/5953fe8a-458c-42d3-954f-05d60f86969e" alt="Lock Screen - Music Playing" width="350"/>

</td>
</tr>
</table>

</div>

## ✨ Features

- 🎵 **Music Search** - Search tracks, artists, albums, and playlists
- 🏠 **Personalized Home** - Featured content, Daily Mix, Top Artists/Albums/Tracks
- 📻 **Radio Stations** - Discover and listen to radio stations
- 🎧 **Music Player** - Mini player and full-screen player with controls
- ⭐ **Favorites** - Save your favorite tracks
- 📚 **Playlists** - Create and manage your playlists
- 🎨 **Dark/Light Theme** - Switch between themes
- 🌍 **Multi-language** - Turkish and English support
- 💾 **Offline Access** - Local database for offline functionality
- 🔄 **Recently Played** - Quick access to your recent tracks

## 🛠️ Tech Stack

- **Language:** Kotlin 2.0.21
- **UI:** Jetpack Compose, Material Design 3
- **Architecture:** Clean Architecture (Data, Domain, Presentation)
- **Pattern:** MVVM (Model-View-ViewModel)
- **Dependency Injection:** Hilt 2.51.1
- **Database:** Room 2.6.1
- **Networking:** Retrofit 2.11.0, OkHttp 4.12.0
- **Image Loading:** Coil 2.7.0
- **Media Player:** ExoPlayer 2.18.7
- **Async:** Kotlin Coroutines 1.9.0, Flow
- **Navigation:** Navigation Compose 2.8.4
- **Data Storage:** DataStore Preferences 1.1.1
- **API:** [Deezer API](https://developers.deezer.com/)

## 📁 Project Structure

```
app/
├── data/
│   ├── local/          # Room database, DAOs, entities
│   ├── remote/         # API service, models
│   ├── mapper/         # Data mappers
│   └── repository/     # Repository implementations
├── domain/
│   ├── model/         # Domain models
│   └── repository/    # Repository interfaces
├── presentation/
│   ├── ui/            # Compose screens and components
│   ├── viewmodel/     # ViewModels
│   └── state/         # UI state classes
├── core/
│   ├── navigation/    # Navigation routes and animations
│   ├── player/        # Music player logic
│   ├── service/       # Background services
│   └── utils/         # Utility classes
└── di/                # Dependency injection modules
```

## 🎯 Architecture

This project follows **Clean Architecture** principles:

- **Presentation Layer:** UI components (Compose), ViewModels, UI states
- **Domain Layer:** Business logic, use cases, domain models
- **Data Layer:** Repositories, data sources (remote API & local database)

### MVVM Pattern

- **Model:** Domain models and data sources
- **View:** Jetpack Compose UI screens
- **ViewModel:** Manages UI state and business logic

## 🔑 API

This app uses the [Deezer API](https://developers.deezer.com/). The API is free and doesn't require authentication for basic features.

## 🌟 Key Features Explained

### Music Player
- Mini player at the bottom for quick access
- Full-screen player with album art rotation
- Play/pause, next/previous track controls
- Seek bar for track navigation
- Background playback support

### Search
- Real-time search with debounce (400ms)
- Search across tracks, artists, albums, and playlists
- Search history saved locally
- Filter by content type

### Playlists
- Create custom playlists
- Add/remove tracks
- Browse public playlists from Deezer
- Manage playlist tracks

## 🧪 Testing

The project includes unit tests and UI tests using:
- JUnit
- Mockito
- Turbine (for Flow testing)
- Truth assertions
- Espresso (for UI tests)


