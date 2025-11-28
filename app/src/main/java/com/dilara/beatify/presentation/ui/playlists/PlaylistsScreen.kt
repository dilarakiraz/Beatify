package com.dilara.beatify.presentation.ui.playlists

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dilara.beatify.presentation.state.PlaylistsUIEvent
import com.dilara.beatify.presentation.ui.components.PlaylistCard
import com.dilara.beatify.presentation.ui.components.common.CreatePlaylistDialog
import com.dilara.beatify.presentation.ui.components.common.EmptySection
import com.dilara.beatify.presentation.ui.components.common.ErrorSection
import com.dilara.beatify.presentation.ui.components.common.FloatingActionButton
import com.dilara.beatify.presentation.ui.components.common.SectionHeader
import com.dilara.beatify.presentation.ui.components.common.TrackCardSkeleton
import com.dilara.beatify.presentation.viewmodel.PlaylistsViewModel
import com.dilara.beatify.ui.theme.DarkBackground

@Composable
fun PlaylistsScreen(
    viewModel: PlaylistsViewModel = hiltViewModel(),
    onPlaylistClick: (Long) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    
    var showCreateDialog by remember { mutableStateOf(false) }
    var playlistName by remember { mutableStateOf("") }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground),
        contentPadding = PaddingValues(
            start = 16.dp,
            end = 16.dp,
            top = 16.dp,
            bottom = 88.dp
        ),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            SectionHeader(
                title = "Çalma Listelerim"
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
                        message = uiState.error ?: "Bilinmeyen hata",
                        onRetry = { viewModel.onEvent(PlaylistsUIEvent.LoadPlaylists) }
                    )
                }
            }

            uiState.playlists.isEmpty() -> {
                item {
                    EmptySection(message = "Henüz çalma listeniz yok")
                }
            }

            else -> {
                itemsIndexed(
                    items = uiState.playlists,
                    key = { _, playlist -> playlist.id }
                ) { index, playlist ->
                    AnimatedVisibility(
                        visible = true,
                        enter = fadeIn() + slideInVertically(
                            initialOffsetY = { it },
                            animationSpec = tween(300, delayMillis = index * 50)
                        )
                    ) {
                        PlaylistCard(
                            playlist = playlist,
                            onClick = {
                                viewModel.onEvent(PlaylistsUIEvent.OnPlaylistClick(playlist.id))
                                onPlaylistClick(playlist.id)
                            }
                        )
                    }
                }
            }
        }
        }

        FloatingActionButton(
            onClick = { showCreateDialog = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        )
        
        if (showCreateDialog) {
            CreatePlaylistDialog(
                playlistName = playlistName,
                onPlaylistNameChange = { playlistName = it },
                onConfirm = {
                    viewModel.onEvent(PlaylistsUIEvent.CreatePlaylist(playlistName))
                    playlistName = ""
                    showCreateDialog = false
                },
                onDismiss = {
                    playlistName = ""
                    showCreateDialog = false
                }
            )
        }
    }
}

