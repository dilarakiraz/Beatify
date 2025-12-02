package com.dilara.beatify.presentation.ui.playlists

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.dilara.beatify.presentation.state.PlaylistsUIEvent
import com.dilara.beatify.presentation.ui.components.PlaylistCard
import com.dilara.beatify.presentation.ui.components.common.CreatePlaylistDialog
import com.dilara.beatify.presentation.ui.components.common.DraggableLazyColumn
import com.dilara.beatify.presentation.ui.components.common.FloatingActionButton
import com.dilara.beatify.presentation.ui.components.common.ScreenStateWrapper
import com.dilara.beatify.presentation.ui.components.common.SectionHeader
import com.dilara.beatify.presentation.viewmodel.PlaylistsViewModel
import com.dilara.beatify.ui.theme.themeBackground

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PlaylistsScreen(
    viewModel: PlaylistsViewModel = hiltViewModel(),
    onPlaylistClick: (Long) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    
    var showCreateDialog by remember { mutableStateOf(false) }
    var playlistName by remember { mutableStateOf("") }

    Box(modifier = Modifier.fillMaxSize()) {
        ScreenStateWrapper(
            isLoading = uiState.isLoading,
            error = uiState.error,
            isEmpty = uiState.playlists.isEmpty(),
            emptyMessage = "Henüz çalma listeniz yok",
            onRetry = { viewModel.onEvent(PlaylistsUIEvent.LoadPlaylists) },
            headerContent = {
                SectionHeader(title = "Çalma Listelerim")
            },
            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                top = 16.dp,
                bottom = 88.dp
            )
        ) {
            DraggableLazyColumn(
                items = uiState.playlists,
                onMove = { fromIndex, toIndex ->
                    viewModel.onEvent(PlaylistsUIEvent.ReorderPlaylists(fromIndex, toIndex))
                },
                modifier = Modifier
                    .fillMaxSize()
                    .background(themeBackground),
                contentPadding = PaddingValues(
                    start = 16.dp,
                    end = 16.dp,
                    top = 16.dp,
                    bottom = 88.dp
                ),
                key = { playlist -> playlist.id },
                headerContent = {
                    SectionHeader(title = "Çalma Listelerim")
                }
            ) { _, playlist, _ ->
                PlaylistCard(
                    playlist = playlist,
                    onClick = {
                        viewModel.onEvent(PlaylistsUIEvent.OnPlaylistClick(playlist.id))
                        onPlaylistClick(playlist.id)
                    },
                    onDelete = {
                        viewModel.onEvent(PlaylistsUIEvent.DeletePlaylist(playlist.id))
                    },
                    modifier = Modifier.fillMaxWidth()
                )
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

