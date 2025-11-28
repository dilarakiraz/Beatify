package com.dilara.beatify.presentation.ui.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInHorizontally
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
import com.dilara.beatify.presentation.state.HomeUIEvent
import com.dilara.beatify.presentation.ui.components.TrackCard
import com.dilara.beatify.presentation.ui.components.TrackCardHorizontal
import com.dilara.beatify.presentation.ui.components.common.EmptySection
import com.dilara.beatify.presentation.ui.components.common.ErrorSection
import com.dilara.beatify.presentation.ui.components.common.SectionHeader
import com.dilara.beatify.presentation.ui.components.common.TrackCardSkeleton
import com.dilara.beatify.presentation.viewmodel.HomeViewModel
import com.dilara.beatify.ui.theme.DarkBackground

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
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
            horizontal = 16.dp,
            vertical = 8.dp
        ),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (!uiState.isLoading && uiState.error == null && uiState.topTracks.isNotEmpty()) {
            item {
                SectionHeader(
                    title = "Featured",
                    subtitle = "Top picks for you"
                )
            }

            items(
                count = minOf(3, uiState.topTracks.size),
                key = { index -> uiState.topTracks[index].id }
            ) { index ->
                val track = uiState.topTracks[index]
                AnimatedVisibility(
                    visible = true,
                    enter = fadeIn() + slideInHorizontally(
                        initialOffsetX = { -it },
                        animationSpec = tween(400, delayMillis = index * 100)
                    )
                ) {
                    TrackCardHorizontal(
                        track = track,
                        onClick = {
                            viewModel.onEvent(HomeUIEvent.OnTrackClick(track.id))
                            onTrackClick(track)
                        }
                    )
                }
            }
        }

        item {
            SectionHeader(
                title = "Top Charts",
                subtitle = "Trending now"
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
                        onRetry = { viewModel.onEvent(HomeUIEvent.Retry) }
                    )
                }
            }

            uiState.topTracks.isEmpty() -> {
                item {
                    EmptySection(message = "No tracks available")
                }
            }

            else -> {
                itemsIndexed(
                    items = uiState.topTracks.drop(3),
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
                                viewModel.onEvent(HomeUIEvent.OnTrackClick(track.id))
                                onTrackClick(track)
                            }
                        )
                    }
                }
            }
        }
    }
}


