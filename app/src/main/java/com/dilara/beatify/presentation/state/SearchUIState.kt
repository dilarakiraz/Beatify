package com.dilara.beatify.presentation.state

import com.dilara.beatify.domain.model.Artist
import com.dilara.beatify.domain.model.Track

data class SearchUIState(
    val searchQuery: String = "",
    val isLoading: Boolean = false,
    val tracks: List<Track> = emptyList(),
    val artists: List<Artist> = emptyList(),
    val suggestedTracks: List<Track> = emptyList(),
    val isLoadingSuggestions: Boolean = false,
    val error: String? = null,
    val isSearchActive: Boolean = false
)

sealed class SearchUIEvent {
    data class OnQueryChange(val query: String) : SearchUIEvent()
    data class OnTrackClick(val trackId: Long) : SearchUIEvent()
    data class OnArtistClick(val artistId: Long) : SearchUIEvent()
    data class OnAlbumClick(val albumId: Long) : SearchUIEvent()
    data object OnSearchFocusChanged : SearchUIEvent()
    data object ClearSearch : SearchUIEvent()
}

