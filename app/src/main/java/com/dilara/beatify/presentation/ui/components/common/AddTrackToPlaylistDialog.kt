package com.dilara.beatify.presentation.ui.components.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dilara.beatify.domain.model.Track
import com.dilara.beatify.presentation.ui.components.BeatifySearchBar
import com.dilara.beatify.presentation.ui.components.TrackCard
import com.dilara.beatify.presentation.viewmodel.SearchViewModel
import com.dilara.beatify.ui.theme.DarkBackground
import com.dilara.beatify.ui.theme.DarkSurface
import com.dilara.beatify.ui.theme.NeonCyan
import com.dilara.beatify.ui.theme.NeonTextPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTrackToPlaylistDialog(
    onTrackSelected: (Track) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val searchViewModel: SearchViewModel = hiltViewModel()
    val uiState by searchViewModel.uiState.collectAsState()

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        modifier = modifier.fillMaxHeight(0.9f),
        containerColor = Transparent,
        dragHandle = {
            Box(
                modifier = Modifier
                    .width(40.dp)
                    .height(4.dp)
                    .padding(vertical = 12.dp)
                    .background(
                        NeonCyan.copy(alpha = 0.4f),
                        RoundedCornerShape(2.dp)
                    )
            )
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            DarkBackground.copy(alpha = 0.98f),
                            DarkSurface.copy(alpha = 0.96f)
                        )
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp, 16.dp)
                ) {
                    androidx.compose.material3.Text(
                        text = "Şarkı Ekle",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = NeonTextPrimary
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
                                com.dilara.beatify.presentation.state.SearchUIEvent.OnQueryChange(query)
                            )
                        },
                        onClearClick = {
                            searchViewModel.onEvent(
                                com.dilara.beatify.presentation.state.SearchUIEvent.ClearSearch
                            )
                        },
                        placeholder = "Şarkı ara..."
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
                        bottom = 8.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
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
                                    onRetry = { /* Retry search */ }
                                )
                            }
                        }

                        uiState.tracks.isEmpty() && uiState.searchQuery.isBlank() -> {
                            item {
                                EmptySection(message = "Müzik aramak için yazmaya başlayın")
                            }
                        }

                        uiState.tracks.isEmpty() -> {
                            item {
                                EmptySection(
                                    message = "\"${uiState.searchQuery}\" için sonuç bulunamadı"
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
                                        onDismiss()
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}


