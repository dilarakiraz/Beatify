package com.dilara.beatify.presentation.state

import com.dilara.beatify.domain.model.PublicPlaylist
import com.dilara.beatify.domain.model.Track

data class PublicPlaylistDetailUIState(
    val isLoading: Boolean = false,
    val playlist: PublicPlaylist? = null,
    val tracks: List<Track> = emptyList(),
    val error: String? = null
)

sealed class PublicPlaylistDetailUIEvent {
    data class LoadPlaylist(val playlistId: Long, val initialPlaylistTitle: String? = null) : PublicPlaylistDetailUIEvent()
    data class OnTrackClick(val trackId: Long) : PublicPlaylistDetailUIEvent()
    data object Retry : PublicPlaylistDetailUIEvent()
}

