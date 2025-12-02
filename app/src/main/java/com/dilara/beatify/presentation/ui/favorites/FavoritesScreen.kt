package com.dilara.beatify.presentation.ui.favorites

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dilara.beatify.presentation.state.FavoritesUIEvent
import com.dilara.beatify.presentation.ui.components.TrackCard
import com.dilara.beatify.presentation.ui.components.common.DraggableLazyColumn
import com.dilara.beatify.presentation.ui.components.common.ScreenStateWrapper
import com.dilara.beatify.presentation.ui.components.common.SectionHeader
import com.dilara.beatify.presentation.viewmodel.FavoritesViewModel
import com.dilara.beatify.ui.theme.themeBackground

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FavoritesScreen(
    viewModel: FavoritesViewModel = hiltViewModel(),
    onTrackClick: (com.dilara.beatify.domain.model.Track) -> Unit = {},
    onArtistClick: (Long) -> Unit = {},
    onAlbumClick: (Long) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    ScreenStateWrapper(
        isLoading = uiState.isLoading,
        error = uiState.error,
        isEmpty = uiState.favoriteTracks.isEmpty(),
        emptyMessage = "Henüz favori şarkınız yok",
        onRetry = { viewModel.onEvent(FavoritesUIEvent.LoadFavorites) },
        headerContent = {
            SectionHeader(title = "Listem")
        },
        contentPadding = PaddingValues(
            start = 16.dp,
            end = 16.dp,
            top = 16.dp,
            bottom = 8.dp
        )
    ) {
        DraggableLazyColumn(
            items = uiState.favoriteTracks,
            onMove = { fromIndex, toIndex ->
                viewModel.onEvent(FavoritesUIEvent.ReorderFavorites(fromIndex, toIndex))
            },
            modifier = Modifier
                .fillMaxSize()
                .background(themeBackground),
            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                top = 16.dp,
                bottom = 8.dp
            ),
            key = { track -> track.id },
            headerContent = {
                SectionHeader(title = "Listem")
            }
        ) { _, track, isDragging ->
            TrackCard(
                track = track,
                onClick = {
                    viewModel.onEvent(FavoritesUIEvent.OnTrackClick(track.id))
                    onTrackClick(track)
                },
                isFavorite = true,
                onFavoriteClick = {
                    viewModel.onEvent(FavoritesUIEvent.ToggleFavorite(track))
                },
                isDragging = isDragging
            )
        }
    }
}

