package com.dilara.beatify.presentation.ui.album

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dilara.beatify.domain.model.Track
import com.dilara.beatify.presentation.state.AlbumDetailUIEvent
import com.dilara.beatify.presentation.ui.components.TrackCard
import com.dilara.beatify.presentation.ui.components.common.DetailScreenContent
import com.dilara.beatify.presentation.ui.components.common.DetailScreenHeader
import com.dilara.beatify.presentation.ui.components.common.EmptySection
import com.dilara.beatify.presentation.ui.components.common.SectionHeader
import com.dilara.beatify.presentation.ui.hooks.useFavoritesState
import com.dilara.beatify.presentation.viewmodel.AlbumDetailViewModel
import com.dilara.beatify.ui.theme.themeBackground
import com.dilara.beatify.core.utils.stringResource
import com.dilara.beatify.R

@Composable
fun AlbumDetailScreen(
    albumId: Long,
    viewModel: AlbumDetailViewModel = hiltViewModel(),
    onTrackClick: (Track, List<Track>) -> Unit = { _, _ -> },
    onArtistClick: (Long) -> Unit = {},
    onNavigateBack: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    val favoritesState = useFavoritesState()

    LaunchedEffect(albumId) {
        viewModel.onEvent(AlbumDetailUIEvent.LoadAlbum(albumId))
    }

    Box(modifier = Modifier.fillMaxSize()) {
        val album = uiState.album
        
        DetailScreenHeader(
            imageUrl = album?.coverXl ?: album?.coverBig ?: album?.coverMedium,
            title = album?.title ?: "",
            subtitle = album?.artist?.name,
            onSubtitleClick = album?.let { { onArtistClick(it.artist.id) } },
            onNavigateBack = onNavigateBack
        )

        DetailScreenContent(
            isLoading = uiState.isLoading,
            error = uiState.error,
            isEmpty = album == null && !uiState.isLoading && uiState.error == null,
            emptyMessage = stringResource(R.string.album_not_found_detail),
            onRetry = { viewModel.onEvent(AlbumDetailUIEvent.LoadAlbum(albumId)) },
            modifier = Modifier.fillMaxSize()
        ) {
            if (album != null) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(themeBackground),
                    contentPadding = PaddingValues(
                        start = 20.dp,
                        end = 20.dp,
                        top = 24.dp,
                        bottom = 160.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                        if (uiState.tracks.isNotEmpty()) {
                            item {
                                SectionHeader(
                                    title = stringResource(R.string.songs),
                                    subtitle = stringResource(R.string.song_count, uiState.tracks.size)
                                )
                            }
                            
                            items(
                                items = uiState.tracks,
                                key = { track -> track.id }
                            ) { track ->
                                TrackCard(
                                    track = track,
                                    onClick = {
                                        onTrackClick(track, uiState.tracks)
                                    },
                                    isFavorite = favoritesState.favoriteTrackIds.contains(track.id),
                                    onFavoriteClick = {
                                        favoritesState.toggleFavorite(track)
                                    },
                                    onArtistClick = {
                                        onArtistClick(track.artist.id)
                                    }
                                )
                            }
                        } else {
                            item {
                                EmptySection(
                                    message = stringResource(R.string.album_tracks_not_found)
                                )
                            }
                        }
                }
            }
        }
    }
}

