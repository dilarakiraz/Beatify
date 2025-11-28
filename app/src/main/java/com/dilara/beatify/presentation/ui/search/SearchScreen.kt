package com.dilara.beatify.presentation.ui.search

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
import com.dilara.beatify.presentation.state.SearchUIEvent
import com.dilara.beatify.presentation.ui.components.BeatifySearchBar
import com.dilara.beatify.presentation.ui.components.TrackCard
import com.dilara.beatify.presentation.ui.components.common.EmptySection
import com.dilara.beatify.presentation.ui.components.common.ErrorSection
import com.dilara.beatify.presentation.ui.components.common.SectionHeader
import com.dilara.beatify.presentation.ui.components.common.TrackCardSkeleton
import com.dilara.beatify.presentation.viewmodel.SearchViewModel
import com.dilara.beatify.ui.theme.DarkBackground

@Composable
fun SearchScreen(
    viewModel: SearchViewModel = hiltViewModel(),
    onTrackClick: (Long) -> Unit = {},
    onArtistClick: (Long) -> Unit = {},
    onAlbumClick: (Long) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground),
        contentPadding = PaddingValues(
            horizontal = 16.dp,
            vertical = 8.dp
        ),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Search Bar
        item {
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

        // Content based on state
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
                        onRetry = {
                            if (uiState.searchQuery.isNotBlank()) {
                                viewModel.onEvent(SearchUIEvent.OnQueryChange(uiState.searchQuery))
                            }
                        }
                    )
                }
            }

            uiState.searchQuery.isBlank() -> {
                // Show suggested tracks
                if (uiState.isLoadingSuggestions) {
                    items(5) {
                        TrackCardSkeleton()
                    }
                } else if (uiState.suggestedTracks.isNotEmpty()) {
                    item {
                        SectionHeader(
                            title = "Suggested for You",
                            subtitle = "Popular tracks"
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
                                    onTrackClick(track.id)
                                }
                            )
                        }
                    }
                } else {
                    item {
                        EmptySection(message = "Start typing to search for music")
                    }
                }
            }

            uiState.tracks.isEmpty() -> {
                item {
                    EmptySection(message = "No results found for \"${uiState.searchQuery}\"")
                }
            }

            else -> {
                item {
                    SectionHeader(
                        title = "Search Results",
                        subtitle = "${uiState.tracks.size} tracks found"
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
                                onTrackClick(track.id)
                            }
                        )
                    }
                }
            }
        }
    }
}

