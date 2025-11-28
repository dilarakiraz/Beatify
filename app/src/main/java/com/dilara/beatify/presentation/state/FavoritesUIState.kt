package com.dilara.beatify.presentation.state

import com.dilara.beatify.domain.model.Track

data class FavoritesUIState(
    val favoriteTracks: List<Track> = emptyList(),
    val favoriteTrackIds: Set<Long> = emptySet(),
    val isLoading: Boolean = false,
    val error: String? = null
)

sealed class FavoritesUIEvent {
    data class OnTrackClick(val trackId: Long) : FavoritesUIEvent()
    data class ToggleFavorite(val track: Track) : FavoritesUIEvent()
    data object LoadFavorites : FavoritesUIEvent()
}

