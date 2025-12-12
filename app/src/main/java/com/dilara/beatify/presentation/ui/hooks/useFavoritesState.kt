package com.dilara.beatify.presentation.ui.hooks

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.dilara.beatify.presentation.state.FavoritesUIEvent
import com.dilara.beatify.presentation.viewmodel.FavoritesViewModel

data class FavoritesState(
    val favoriteTrackIds: Set<Long>,
    val toggleFavorite: (com.dilara.beatify.domain.model.Track) -> Unit
)

@Composable
fun useFavoritesState(): FavoritesState {
    val favoritesViewModel: FavoritesViewModel = hiltViewModel()
    val favoritesState by favoritesViewModel.uiState.collectAsState()

    return FavoritesState(
        favoriteTrackIds = favoritesState.favoriteTrackIds,
        toggleFavorite = { track ->
            favoritesViewModel.onEvent(FavoritesUIEvent.ToggleFavorite(track))
        }
    )
}