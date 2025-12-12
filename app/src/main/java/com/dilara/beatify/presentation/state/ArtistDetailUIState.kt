package com.dilara.beatify.presentation.state

import com.dilara.beatify.domain.model.Album
import com.dilara.beatify.domain.model.Artist
import com.dilara.beatify.domain.model.Track

data class ArtistDetailUIState(
    val artist: Artist? = null,
    val topTracks: List<Track> = emptyList(),
    val albums: List<Album> = emptyList(),
    val relatedArtists: List<Artist> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

sealed class ArtistDetailUIEvent {
    data class LoadArtist(val artistId: Long) : ArtistDetailUIEvent()
    data class Refresh(val artistId: Long) : ArtistDetailUIEvent()
}