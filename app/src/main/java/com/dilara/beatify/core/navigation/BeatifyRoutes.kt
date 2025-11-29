package com.dilara.beatify.core.navigation

sealed class BeatifyRoutes(val route: String) {
    object Splash : BeatifyRoutes("splash")
    object Home : BeatifyRoutes("home")
    object Search : BeatifyRoutes("search")
    object Favorites : BeatifyRoutes("favorites")
    object Playlists : BeatifyRoutes("playlists")
    object PlaylistDetail : BeatifyRoutes("playlist_detail/{playlistId}") {
        fun createRoute(playlistId: Long) = "playlist_detail/$playlistId"
    }
    object Profile : BeatifyRoutes("profile")
    object TrackDetail : BeatifyRoutes("track/{trackId}") {
        fun createRoute(trackId: Long) = "track/$trackId"
    }
    object ArtistDetail : BeatifyRoutes("artist/{artistId}") {
        fun createRoute(artistId: Long) = "artist/$artistId"
    }
    object AlbumDetail : BeatifyRoutes("album/{albumId}") {
        fun createRoute(albumId: Long) = "album/$albumId"
    }
}


