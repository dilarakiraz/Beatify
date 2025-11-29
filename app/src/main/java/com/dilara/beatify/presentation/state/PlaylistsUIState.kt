package com.dilara.beatify.presentation.state

import com.dilara.beatify.domain.model.Playlist

data class PlaylistsUIState(
    val playlists: List<Playlist> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

sealed class PlaylistsUIEvent {
    data object LoadPlaylists : PlaylistsUIEvent()
    data class CreatePlaylist(val name: String) : PlaylistsUIEvent()
    data class DeletePlaylist(val playlistId: Long) : PlaylistsUIEvent()
    data class OnPlaylistClick(val playlistId: Long) : PlaylistsUIEvent()
}


