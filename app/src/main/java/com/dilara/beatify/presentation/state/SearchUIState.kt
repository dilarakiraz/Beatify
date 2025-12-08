package com.dilara.beatify.presentation.state

import com.dilara.beatify.domain.model.Album
import com.dilara.beatify.domain.model.Artist
import com.dilara.beatify.domain.model.PublicPlaylist
import com.dilara.beatify.domain.model.SearchHistory
import com.dilara.beatify.domain.model.Track

enum class SearchType {
    ALL, TRACKS, ARTISTS, ALBUMS, PLAYLISTS
}

data class SearchUIState(
    val searchQuery: String = "",
    val isLoading: Boolean = false,
    val searchType: SearchType = SearchType.ALL,
    val tracks: List<Track> = emptyList(),
    val artists: List<Artist> = emptyList(),
    val albums: List<Album> = emptyList(),
    val playlists: List<PublicPlaylist> = emptyList(),
    val suggestedTracks: List<Track> = emptyList(),
    val searchHistory: List<SearchHistory> = emptyList(),
    val isLoadingSuggestions: Boolean = false,
    val error: String? = null,
    val isSearchActive: Boolean = false
)

sealed class SearchUIEvent {
    data class OnQueryChange(val query: String) : SearchUIEvent()
    data class OnSearchTypeChange(val searchType: SearchType) : SearchUIEvent()
    data class OnTrackClick(val trackId: Long) : SearchUIEvent()
    data class OnArtistClick(val artistId: Long) : SearchUIEvent()
    data class OnAlbumClick(val albumId: Long) : SearchUIEvent()
    data class OnPlaylistClick(val playlistId: Long) : SearchUIEvent()
    data class OnSearchHistoryClick(val query: String) : SearchUIEvent()
    data class OnSearchHistoryTrackClick(val track: Track) : SearchUIEvent()
    data object OnSearchFocusChanged : SearchUIEvent()
    data object ClearSearch : SearchUIEvent()
    data class DeleteSearchHistory(val searchHistoryId: Long) : SearchUIEvent()
}

