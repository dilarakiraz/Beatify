package com.dilara.beatify.presentation.state

import com.dilara.beatify.domain.model.Album
import com.dilara.beatify.domain.model.Artist
import com.dilara.beatify.domain.model.Track
import com.dilara.beatify.domain.model.Genre
import com.dilara.beatify.domain.model.Radio

data class HomeUIState(
    val isLoading: Boolean = false,
    val topTracks: List<Track> = emptyList(),
    val topAlbums: List<Album> = emptyList(),
    val topArtists: List<Artist> = emptyList(),
    val genres: List<Genre> = emptyList(),
    val radios: List<Radio> = emptyList(),
    val dailyMix: List<Track> = emptyList(),
    val recentTracks: List<Track> = emptyList(),
    val error: String? = null
)

sealed class HomeUIEvent {
    data class OnTrackClick(val trackId: Long) : HomeUIEvent()
    data class OnArtistClick(val artistId: Long) : HomeUIEvent()
    data class OnAlbumClick(val albumId: Long) : HomeUIEvent()
    data class OnGenreClick(val genreId: Long) : HomeUIEvent()
    data class OnRadioClick(val radioId: Long) : HomeUIEvent()
    data object Retry : HomeUIEvent()
}


