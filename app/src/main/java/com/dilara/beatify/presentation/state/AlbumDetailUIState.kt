package com.dilara.beatify.presentation.state

import com.dilara.beatify.domain.model.Album
import com.dilara.beatify.domain.model.Track

data class AlbumDetailUIState(
    val album: Album? = null,
    val tracks: List<Track> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

sealed class AlbumDetailUIEvent {
    data class LoadAlbum(val albumId: Long) : AlbumDetailUIEvent()
    data class Refresh(val albumId: Long) : AlbumDetailUIEvent()
}