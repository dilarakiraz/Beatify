package com.dilara.beatify.presentation.state

import com.dilara.beatify.domain.model.Track

data class HomeUIState(
    val isLoading: Boolean = false,
    val topTracks: List<Track> = emptyList(),
    val dailyMix: List<Track> = emptyList(),
    val recentTracks: List<Track> = emptyList(),
    val error: String? = null
)

sealed class HomeUIEvent {
    data class OnTrackClick(val trackId: Long) : HomeUIEvent()
    data class OnArtistClick(val artistId: Long) : HomeUIEvent()
    data class OnAlbumClick(val albumId: Long) : HomeUIEvent()
    data object Retry : HomeUIEvent()
}

