package com.dilara.beatify.presentation.ui.favorites

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dilara.beatify.presentation.state.FavoritesUIEvent
import com.dilara.beatify.presentation.ui.components.TrackCard
import com.dilara.beatify.presentation.ui.components.common.EmptySection
import com.dilara.beatify.presentation.ui.components.common.ErrorSection
import com.dilara.beatify.presentation.ui.components.common.SectionHeader
import com.dilara.beatify.presentation.ui.components.common.TrackCardSkeleton
import com.dilara.beatify.presentation.viewmodel.FavoritesViewModel
import com.dilara.beatify.ui.theme.DarkBackground

@Composable
fun FavoritesScreen(
    viewModel: FavoritesViewModel = hiltViewModel(),
    onTrackClick: (com.dilara.beatify.domain.model.Track) -> Unit = {},
    onArtistClick: (Long) -> Unit = {},
    onAlbumClick: (Long) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground),
        contentPadding = PaddingValues(
            start = 16.dp,
            end = 16.dp,
            top = 16.dp,
            bottom = 8.dp
        ),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            SectionHeader(
                title = "Listem",
            )
        }

        when {
            uiState.isLoading -> {
                items(5) {
                    TrackCardSkeleton()
                }
            }

            uiState.error != null -> {
                item {
                    ErrorSection(
                        message = uiState.error ?: "Unknown error",
                        onRetry = { viewModel.onEvent(FavoritesUIEvent.LoadFavorites) }
                    )
                }
            }

            uiState.favoriteTracks.isEmpty() -> {
                item {
                    EmptySection(message = "Henüz favori şarkınız yok")
                }
            }

            else -> {
                itemsIndexed(
                    items = uiState.favoriteTracks,
                    key = { _, track -> track.id }
                ) { index, track ->
                    AnimatedVisibility(
                        visible = true,
                        enter = fadeIn() + slideInVertically(
                            initialOffsetY = { it },
                            animationSpec = tween(300, delayMillis = index * 50)
                        )
                    ) {
                        TrackCard(
                            track = track,
                            onClick = {
                                viewModel.onEvent(FavoritesUIEvent.OnTrackClick(track.id))
                                onTrackClick(track)
                            },
                            isFavorite = true,
                            onFavoriteClick = {
                                viewModel.onEvent(FavoritesUIEvent.ToggleFavorite(track))
                            }
                        )
                    }
                }
            }
        }
    }
}

