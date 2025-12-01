package com.dilara.beatify.presentation.ui.search

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dilara.beatify.presentation.state.SearchUIEvent
import com.dilara.beatify.presentation.ui.components.ArtistCard
import com.dilara.beatify.presentation.ui.components.BeatifySearchBar
import com.dilara.beatify.presentation.ui.components.TrackCard
import com.dilara.beatify.presentation.ui.components.common.CircularSearchHistoryChip
import com.dilara.beatify.presentation.ui.components.common.EmptySection
import com.dilara.beatify.presentation.ui.components.common.ErrorSection
import com.dilara.beatify.presentation.ui.components.common.SectionHeader
import com.dilara.beatify.presentation.ui.components.common.TrackCardSkeleton
import com.dilara.beatify.presentation.state.FavoritesUIEvent
import com.dilara.beatify.presentation.viewmodel.FavoritesViewModel
import com.dilara.beatify.presentation.viewmodel.SearchViewModel
import com.dilara.beatify.ui.theme.DarkBackground

@Composable
fun SearchScreen(
    viewModel: SearchViewModel = hiltViewModel(),
    onTrackClick: (com.dilara.beatify.domain.model.Track) -> Unit = {},
    onArtistClick: (Long) -> Unit = {},
    onAlbumClick: (Long) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    val favoritesViewModel: FavoritesViewModel = hiltViewModel()
    val favoritesState by favoritesViewModel.uiState.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground),
        contentPadding = PaddingValues(
            start = 16.dp,
            end = 16.dp,
            top = 0.dp,
            bottom = 8.dp
        ),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        stickyHeader {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(DarkBackground)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                BeatifySearchBar(
                    query = uiState.searchQuery,
                    onQueryChange = { query ->
                        viewModel.onEvent(SearchUIEvent.OnQueryChange(query))
                    },
                    onClearClick = {
                        viewModel.onEvent(SearchUIEvent.ClearSearch)
                    }
                )
            }
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
                        message = uiState.error ?: "Bilinmeyen hata",
                        onRetry = {
                            if (uiState.searchQuery.isNotBlank()) {
                                viewModel.onEvent(SearchUIEvent.OnQueryChange(uiState.searchQuery))
                            }
                        }
                    )
                }
            }

            uiState.searchQuery.isBlank() -> {
                if (uiState.isLoadingSuggestions) {
                    items(5) {
                        TrackCardSkeleton()
                    }
                } else {
                    if (uiState.searchHistory.isNotEmpty()) {
                        item {
                            SectionHeader(
                                title = "Son Aramalarım"
                            )
                        }
                        
                        item {
                            LazyRow(
                                horizontalArrangement = Arrangement.spacedBy(16.dp),
                                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp)
                            ) {
                                items(
                                    items = uiState.searchHistory,
                                    key = { history -> history.id }
                                ) { history ->
                                    CircularSearchHistoryChip(
                                        track = history.track,
                                        onClick = {
                                            viewModel.onEvent(SearchUIEvent.OnSearchHistoryTrackClick(history.track))
                                        },
                                        onDelete = {
                                            viewModel.onEvent(SearchUIEvent.DeleteSearchHistory(history.id))
                                        }
                                    )
                                }
                            }
                        }
                        
                        item {
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                    
                    if (uiState.suggestedTracks.isNotEmpty()) {
                        item {
                            SectionHeader(
                                title = "Sizin İçin Önerilenler"
                            )
                        }
                        
                        itemsIndexed(
                            items = uiState.suggestedTracks,
                            key = { _, track -> track.id }
                        ) { index, track ->
                            AnimatedVisibility(
                                visible = true,
                                enter = fadeIn() + slideInVertically(
                                    initialOffsetY = { it },
                                    animationSpec = tween(300, delayMillis = index * 40)
                                )
                            ) {
                                TrackCard(
                                    track = track,
                                    onClick = {
                                        viewModel.onEvent(SearchUIEvent.OnTrackClick(track.id))
                                        onTrackClick(track)
                                    },
                                    isFavorite = favoritesState.favoriteTrackIds.contains(track.id),
                                    onFavoriteClick = {
                                        favoritesViewModel.onEvent(FavoritesUIEvent.ToggleFavorite(track))
                                    }
                                )
                            }
                        }
                    } else if (uiState.searchHistory.isEmpty()) {
                        item {
                            EmptySection(message = "Müzik aramak için yazmaya başlayın")
                        }
                    }
                }
            }

            uiState.tracks.isEmpty() && uiState.artists.isEmpty() -> {
                item {
                    EmptySection(message = "\"${uiState.searchQuery}\" için sonuç bulunamadı")
                }
            }

            else -> {
                if (uiState.artists.isNotEmpty()) {
                    item {
                        SectionHeader(
                            title = "Sanatçılar",
                            subtitle = "${uiState.artists.size} sanatçı bulundu"
                        )
                    }

                    item {
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            contentPadding = PaddingValues(horizontal = 4.dp)
                        ) {
                            items(
                                items = uiState.artists,
                                key = { artist -> artist.id }
                            ) { artist ->
                                ArtistCard(
                                    artist = artist,
                                    onClick = {
                                        viewModel.onEvent(SearchUIEvent.OnArtistClick(artist.id))
                                        onArtistClick(artist.id)
                                    },
                                    modifier = Modifier.size(width = 120.dp, height = 120.dp)
                                )
                            }
                        }
                    }
                }

                if (uiState.tracks.isNotEmpty()) {
                    item {
                        SectionHeader(
                            title = "Şarkılar",
                            subtitle = "${uiState.tracks.size} şarkı bulundu"
                        )
                    }

                    itemsIndexed(
                        items = uiState.tracks,
                        key = { _, track -> track.id }
                    ) { index, track ->
                        AnimatedVisibility(
                            visible = true,
                            enter = fadeIn() + slideInVertically(
                                initialOffsetY = { it },
                                animationSpec = tween(300, delayMillis = index * 30)
                            )
                        ) {
                            TrackCard(
                                track = track,
                                onClick = {
                                    viewModel.onEvent(SearchUIEvent.OnTrackClick(track.id))
                                    onTrackClick(track)
                                },
                                isFavorite = favoritesState.favoriteTrackIds.contains(track.id),
                                onFavoriteClick = {
                                    favoritesViewModel.onEvent(FavoritesUIEvent.ToggleFavorite(track))
                                },
                                onArtistClick = {
                                    viewModel.onEvent(SearchUIEvent.OnArtistClick(track.artist.id))
                                    onArtistClick(track.artist.id)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

