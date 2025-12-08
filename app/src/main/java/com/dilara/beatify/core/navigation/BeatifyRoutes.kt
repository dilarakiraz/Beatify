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
    object GenreDetail : BeatifyRoutes("genre/{genreId}") {
        fun createRoute(genreId: Long) = "genre/$genreId"
    }
    object RadioDetail : BeatifyRoutes("radio/{radioId}/{title}") {
        fun createRoute(radioId: Long, radioTitle: String? = null) = 
            if (radioTitle != null) {
                "radio/$radioId/${java.net.URLEncoder.encode(radioTitle, "UTF-8")}"
            } else {
                "radio/$radioId/_"
            }
    }
}


