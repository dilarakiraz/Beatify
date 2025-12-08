package com.dilara.beatify.presentation.ui.components.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dilara.beatify.domain.model.Track
import com.dilara.beatify.presentation.state.SearchUIEvent
import com.dilara.beatify.presentation.ui.components.BeatifySearchBar
import com.dilara.beatify.presentation.ui.components.TrackCard
import com.dilara.beatify.presentation.viewmodel.SearchViewModel
import com.dilara.beatify.ui.theme.themeTextPrimary
import com.dilara.beatify.core.utils.stringResource
import com.dilara.beatify.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTrackToPlaylistDialog(
    onTrackSelected: (Track) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    onArtistClick: ((Long) -> Unit)? = null,
    onTrackClick: ((Track) -> Unit)? = null
) {
    val searchViewModel: SearchViewModel = hiltViewModel()
    val uiState by searchViewModel.uiState.collectAsState()

    BeatifyBottomSheet(
        onDismiss = onDismiss,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .offset(y = (-32).dp)
                .padding(top = 32.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp)
            ) {
                Text(
                    text = stringResource(R.string.add_track),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = themeTextPrimary
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 8.dp)
            ) {
                BeatifySearchBar(
                    query = uiState.searchQuery,
                    onQueryChange = { query ->
                        searchViewModel.onEvent(
                            SearchUIEvent.OnQueryChange(query)
                        )
                    },
                    onClearClick = {
                        searchViewModel.onEvent(
                            SearchUIEvent.ClearSearch
                        )
                    },
                    placeholder = stringResource(R.string.search_track_hint)
                )
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentPadding = PaddingValues(
                    start = 24.dp,
                    end = 24.dp,
                    top = 8.dp,
                    bottom = 0.dp
                ),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                when {
                    uiState.isLoading -> {
                        items(5) {
                            LoadingSkeleton()
                        }
                    }

                    uiState.error != null -> {
                        item {
                            ErrorSection(
                                message = uiState.error ?: stringResource(R.string.error_unknown),
                                onRetry = { /* Retry search */ }
                            )
                        }
                    }

                    uiState.tracks.isEmpty() && uiState.searchQuery.isBlank() -> {
                        item {
                            EmptySection(message = stringResource(R.string.search_hint))
                        }
                    }

                    uiState.tracks.isEmpty() -> {
                        item {
                            EmptySection(
                                message = stringResource(R.string.no_results_found, uiState.searchQuery)
                            )
                        }
                    }

                    else -> {
                        items(
                            items = uiState.tracks,
                            key = { track -> track.id }
                        ) { track ->
                            TrackCard(
                                track = track,
                                onClick = {
                                    onTrackSelected(track)
                                    onTrackClick?.invoke(track)
                                    onDismiss()
                                },
                                onArtistClick = onArtistClick?.let { callback ->
                                    {
                                        callback(track.artist.id)
                                        onDismiss()
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}


