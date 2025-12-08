package com.dilara.beatify.presentation.ui.playlist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dilara.beatify.domain.model.Track
import com.dilara.beatify.presentation.state.PublicPlaylistDetailUIEvent
import com.dilara.beatify.presentation.ui.components.TrackCard
import com.dilara.beatify.presentation.ui.components.common.LoadingSkeleton
import com.dilara.beatify.presentation.ui.components.common.SectionHeader
import com.dilara.beatify.presentation.ui.components.common.SimpleDetailHeader
import com.dilara.beatify.presentation.ui.hooks.useFavoritesState
import com.dilara.beatify.presentation.viewmodel.PublicPlaylistDetailViewModel
import com.dilara.beatify.ui.theme.themeBackground
import com.dilara.beatify.ui.theme.themeTextPrimary
import com.dilara.beatify.core.utils.stringResource
import com.dilara.beatify.R

@Composable
fun PublicPlaylistDetailScreen(
    playlistId: Long,
    initialPlaylistTitle: String? = null,
    viewModel: PublicPlaylistDetailViewModel = hiltViewModel(),
    onTrackClick: (Track, List<Track>) -> Unit = { _, _ -> },
    onNavigateBack: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    val favoritesState = useFavoritesState()
    
    val rememberedTitle = remember(playlistId) { initialPlaylistTitle }

    LaunchedEffect(playlistId) {
        viewModel.onEvent(PublicPlaylistDetailUIEvent.LoadPlaylist(playlistId, initialPlaylistTitle))
    }
    
    val displayTitle = rememberedTitle ?: uiState.playlist?.title ?: stringResource(R.string.playlist)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(themeBackground)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                top = 176.dp,
                bottom = 160.dp
            ),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (uiState.isLoading && uiState.tracks.isEmpty()) {
                items(
                    count = 5,
                    key = { index -> "loading_skeleton_$index" }
                ) {
                    LoadingSkeleton()
                }
            }

            if (uiState.error != null && uiState.tracks.isEmpty()) {
                item(key = "error_section") {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = uiState.error ?: stringResource(R.string.error_occurred),
                            color = themeTextPrimary,
                            fontSize = 16.sp
                        )
                    }
                }
            }

            if (!uiState.isLoading || uiState.tracks.isNotEmpty()) {
                if (uiState.tracks.isNotEmpty()) {
                    item(key = "tracks_header") {
                        SectionHeader(title = stringResource(R.string.tracks))
                    }

                    items(
                        items = uiState.tracks,
                        key = { track -> "playlist_track_${track.id}" }
                    ) { track ->
                        TrackCard(
                            track = track,
                            onClick = {
                                viewModel.onEvent(PublicPlaylistDetailUIEvent.OnTrackClick(track.id))
                                onTrackClick(track, uiState.tracks)
                            },
                            isFavorite = favoritesState.favoriteTrackIds.contains(track.id),
                            onFavoriteClick = {
                                favoritesState.toggleFavorite(track)
                            }
                        )
                    }
                }
            }
        }
        
        SimpleDetailHeader(
            title = displayTitle,
            onNavigateBack = onNavigateBack
        )
    }
}

