package com.dilara.beatify.presentation.state

import com.dilara.beatify.domain.model.Genre
import com.dilara.beatify.domain.model.Artist
import com.dilara.beatify.domain.model.Track

data class GenreDetailUIState(
    val isLoading: Boolean = false,
    val genre: Genre? = null,
    val artists: List<Artist> = emptyList(),
    val radioTracks: List<Track> = emptyList(),
    val error: String? = null
)

sealed class GenreDetailUIEvent {
    data class LoadGenre(val genreId: Long) : GenreDetailUIEvent()
    data class OnArtistClick(val artistId: Long) : GenreDetailUIEvent()
    data class OnTrackClick(val trackId: Long) : GenreDetailUIEvent()
    data object Retry : GenreDetailUIEvent()
}



