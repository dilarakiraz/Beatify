package com.dilara.beatify.presentation.state

import com.dilara.beatify.domain.model.Playlist
import com.dilara.beatify.domain.model.Track

data class PlaylistDetailUIState(
    val playlist: Playlist? = null,
    val tracks: List<Track> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

sealed class PlaylistDetailUIEvent {
    data class LoadPlaylist(val playlistId: Long) : PlaylistDetailUIEvent()
    data class AddTrack(val track: Track) : PlaylistDetailUIEvent()
    data class RemoveTrack(val trackId: Long) : PlaylistDetailUIEvent()
    data class OnTrackClick(val trackId: Long) : PlaylistDetailUIEvent()
    data class DeletePlaylist(val playlistId: Long) : PlaylistDetailUIEvent()
    data class UpdatePlaylistName(val playlistId: Long, val name: String) : PlaylistDetailUIEvent()
    data class ReorderTracks(val fromIndex: Int, val toIndex: Int) : PlaylistDetailUIEvent()
}

