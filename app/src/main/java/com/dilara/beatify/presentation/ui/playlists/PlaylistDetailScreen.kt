package com.dilara.beatify.presentation.ui.playlists

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.dilara.beatify.domain.model.Track
import com.dilara.beatify.presentation.state.PlaylistDetailUIEvent
import com.dilara.beatify.presentation.ui.components.TrackCard
import com.dilara.beatify.presentation.ui.components.common.AddTrackToPlaylistDialog
import com.dilara.beatify.presentation.ui.components.common.BackButton
import com.dilara.beatify.presentation.ui.components.common.DraggableLazyColumn
import com.dilara.beatify.presentation.ui.components.common.EmptySection
import com.dilara.beatify.presentation.ui.components.common.ErrorSection
import com.dilara.beatify.presentation.ui.components.common.FloatingActionButton
import com.dilara.beatify.presentation.ui.components.common.TrackCardSkeleton
import com.dilara.beatify.presentation.viewmodel.PlaylistDetailViewModel
import com.dilara.beatify.ui.theme.DarkBackground
import com.dilara.beatify.ui.theme.DarkSurface
import com.dilara.beatify.ui.theme.NeonCyan
import com.dilara.beatify.ui.theme.NeonTextPrimary
import com.dilara.beatify.ui.theme.NeonTextSecondary

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PlaylistDetailScreen(
    playlistId: Long,
    viewModel: PlaylistDetailViewModel = hiltViewModel(),
    onTrackClick: (Track, List<Track>) -> Unit = { _, _ -> },
    onNavigateBack: () -> Unit = {},
    onArtistClick: ((Long) -> Unit)? = null
) {
    val uiState by viewModel.uiState.collectAsState()

    var showAddTrackDialog by remember { mutableStateOf(false) }
    var isEditingName by remember { mutableStateOf(false) }
    var editedName by remember { mutableStateOf("") }

    LaunchedEffect(playlistId) {
        viewModel.onEvent(PlaylistDetailUIEvent.LoadPlaylist(playlistId))
    }

    LaunchedEffect(uiState.playlist?.name) {
        editedName = uiState.playlist?.name ?: ""
        isEditingName = false
    }

    Box(modifier = Modifier.fillMaxSize()) {
        val playlist = uiState.playlist
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(320.dp)
        ) {
                    if (playlist != null && playlist.coverUrl != null) {
                        AsyncImage(
                            model = playlist.coverUrl,
                            contentDescription = playlist.name,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp)),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    brush = Brush.horizontalGradient(
                                        colors = listOf(
                                            DarkSurface.copy(alpha = 0.8f),
                                            DarkSurface.copy(alpha = 0.6f)
                                        )
                                    )
                                )
                                .clip(RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp))
                        )
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                brush = Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        Color.Black.copy(alpha = 0.6f),
                                        Color.Black.copy(alpha = 0.8f)
                                    )
                                )
                            )
                    )

                    Box(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .statusBarsPadding()
                            .padding(16.dp)
                            .zIndex(1f)
                    ) {
                        BackButton(onClick = onNavigateBack)
                    }

                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .fillMaxWidth()
                            .padding(24.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (isEditingName) {
                                TextField(
                                    value = editedName,
                                    onValueChange = { editedName = it },
                                    modifier = Modifier.weight(1f),
                                    colors = TextFieldDefaults.colors(
                                        focusedContainerColor = DarkSurface.copy(alpha = 0.9f),
                                        unfocusedContainerColor = DarkSurface.copy(alpha = 0.9f),
                                        focusedIndicatorColor = NeonCyan,
                                        unfocusedIndicatorColor = Color.Transparent,
                                        focusedTextColor = NeonTextPrimary,
                                        unfocusedTextColor = NeonTextPrimary
                                    ),
                                    shape = RoundedCornerShape(12.dp),
                                    singleLine = true,
                                    textStyle = androidx.compose.ui.text.TextStyle(
                                        fontSize = 28.sp,
                                        fontWeight = FontWeight.Bold
                                    ),
                                    keyboardOptions = KeyboardOptions(
                                        imeAction = ImeAction.Done
                                    ),
                                    keyboardActions = KeyboardActions(
                                        onDone = {
                                            val currentPlaylist = uiState.playlist
                                            if (editedName.isNotBlank() && editedName != currentPlaylist?.name) {
                                                viewModel.onEvent(
                                                    PlaylistDetailUIEvent.UpdatePlaylistName(
                                                        playlistId,
                                                        editedName
                                                    )
                                                )
                                            }
                                            isEditingName = false
                                        }
                                    ),
                                    trailingIcon = {
                                        Row {
                                            IconButton(
                                                onClick = {
                                                    val currentPlaylist = uiState.playlist
                                                    if (editedName.isNotBlank() && editedName != currentPlaylist?.name) {
                                                        viewModel.onEvent(
                                                            PlaylistDetailUIEvent.UpdatePlaylistName(
                                                                playlistId,
                                                                editedName
                                                            )
                                                        )
                                                    }
                                                    isEditingName = false
                                                }
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.Check,
                                                    contentDescription = "Kaydet",
                                                    tint = NeonCyan
                                                )
                                            }
                                            IconButton(
                                                onClick = {
                                                    editedName = uiState.playlist?.name ?: ""
                                                    isEditingName = false
                                                }
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.Clear,
                                                    contentDescription = "İptal",
                                                    tint = NeonTextSecondary
                                                )
                                            }
                                        }
                                    }
                                )
                            } else {
                                Text(
                                    text = uiState.playlist?.name ?: "Çalma Listesi",
                                    fontSize = 32.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = NeonTextPrimary,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier.weight(1f)
                                )
                                
                                IconButton(
                                    onClick = {
                                        editedName = uiState.playlist?.name ?: ""
                                        isEditingName = true
                                    },
                                    modifier = Modifier
                                        .size(48.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(DarkSurface.copy(alpha = 0.8f))
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Edit,
                                        contentDescription = "Düzenle",
                                        tint = NeonCyan,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "${uiState.tracks.size} şarkı",
                            fontSize = 16.sp,
                            color = NeonTextSecondary,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
        
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 320.dp)
        ) {
            when {
                uiState.isLoading -> {
                    androidx.compose.foundation.lazy.LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(DarkBackground),
                        contentPadding = PaddingValues(
                            start = 20.dp,
                            end = 20.dp,
                            top = 24.dp,
                            bottom = 160.dp
                        ),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(5) {
                            TrackCardSkeleton()
                        }
                    }
                }
                
                uiState.error != null -> {
                    androidx.compose.foundation.lazy.LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(DarkBackground),
                        contentPadding = PaddingValues(
                            start = 20.dp,
                            end = 20.dp,
                            top = 24.dp,
                            bottom = 160.dp
                        )
                    ) {
                        item {
                            ErrorSection(
                                message = uiState.error ?: "Bilinmeyen hata",
                                onRetry = { viewModel.onEvent(PlaylistDetailUIEvent.LoadPlaylist(playlistId)) }
                            )
                        }
                    }
                }
                
                uiState.tracks.isEmpty() -> {
                    androidx.compose.foundation.lazy.LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(DarkBackground),
                        contentPadding = PaddingValues(
                            start = 20.dp,
                            end = 20.dp,
                            top = 24.dp,
                            bottom = 160.dp
                        )
                    ) {
                        item {
                            EmptySection(
                                message = "Bu çalma listesinde henüz şarkı yok. Şarkı eklemek için + butonuna tıklayın."
                            )
                        }
                    }
                }
                
                else -> {
                    DraggableLazyColumn(
                        items = uiState.tracks,
                        onMove = { fromIndex, toIndex ->
                            viewModel.onEvent(PlaylistDetailUIEvent.ReorderTracks(fromIndex, toIndex))
                        },
                        modifier = Modifier
                            .fillMaxSize()
                            .background(DarkBackground),
                        contentPadding = PaddingValues(
                            start = 20.dp,
                            end = 20.dp,
                            top = 24.dp,
                            bottom = 160.dp
                        ),
                        key = { track -> track.id },
                        headerContent = {
                            Text(
                                text = "Şarkılar",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                color = NeonTextPrimary,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                        }
                    ) { _, track, isDragging ->
                        TrackCard(
                            track = track,
                            onClick = {
                                viewModel.onEvent(PlaylistDetailUIEvent.OnTrackClick(track.id))
                                onTrackClick(track, uiState.tracks)
                            },
                            onDelete = {
                                viewModel.onEvent(PlaylistDetailUIEvent.RemoveTrack(track.id))
                            },
                            isDragging = isDragging
                        )
                    }
                }
            }
        }

        FloatingActionButton(
            onClick = { showAddTrackDialog = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        )
    }

    if (showAddTrackDialog) {
        AddTrackToPlaylistDialog(
            onTrackSelected = { track ->
                viewModel.onEvent(PlaylistDetailUIEvent.AddTrack(track))
                showAddTrackDialog = false
            },
            onDismiss = { showAddTrackDialog = false },
            onArtistClick = onArtistClick,
            onTrackClick = { track ->
                onTrackClick(track, uiState.tracks + track)
            }
        )
    }
}
