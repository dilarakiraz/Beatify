package com.dilara.beatify.presentation.ui.album

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.dilara.beatify.domain.model.Track
import com.dilara.beatify.presentation.state.AlbumDetailUIEvent
import com.dilara.beatify.presentation.state.FavoritesUIEvent
import com.dilara.beatify.presentation.ui.components.TrackCard
import com.dilara.beatify.presentation.ui.components.common.BackButton
import com.dilara.beatify.presentation.ui.components.common.EmptySection
import com.dilara.beatify.presentation.ui.components.common.ErrorSection
import com.dilara.beatify.presentation.ui.components.common.SectionHeader
import com.dilara.beatify.presentation.ui.components.common.TrackCardSkeleton
import com.dilara.beatify.presentation.viewmodel.AlbumDetailViewModel
import com.dilara.beatify.presentation.viewmodel.FavoritesViewModel
import com.dilara.beatify.ui.theme.DarkBackground
import com.dilara.beatify.ui.theme.DarkSurface
import com.dilara.beatify.ui.theme.NeonTextPrimary
import com.dilara.beatify.ui.theme.NeonTextSecondary

@Composable
fun AlbumDetailScreen(
    albumId: Long,
    viewModel: AlbumDetailViewModel = hiltViewModel(),
    onTrackClick: (Track, List<Track>) -> Unit = { _, _ -> },
    onArtistClick: (Long) -> Unit = {},
    onNavigateBack: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    val favoritesViewModel: FavoritesViewModel = hiltViewModel()
    val favoritesState by favoritesViewModel.uiState.collectAsState()

    LaunchedEffect(albumId) {
        viewModel.onEvent(AlbumDetailUIEvent.LoadAlbum(albumId))
    }

    Box(modifier = Modifier.fillMaxSize()) {
        val album = uiState.album
        
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(320.dp)
        ) {
            if (album != null) {
                if (album.coverXl != null || album.coverBig != null || album.coverMedium != null) {
                    AsyncImage(
                        model = album.coverXl ?: album.coverBig ?: album.coverMedium,
                        contentDescription = album.title,
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
                    Text(
                        text = album.title,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = NeonTextPrimary,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = album.artist.name,
                        fontSize = 18.sp,
                        color = NeonTextSecondary,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.clickable {
                            onArtistClick(album.artist.id)
                        }
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    if (uiState.tracks.isNotEmpty()) {
                        Text(
                            text = "${uiState.tracks.size} şarkı",
                            fontSize = 16.sp,
                            color = NeonTextSecondary,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 320.dp)
        ) {
            when {
                uiState.isLoading -> {
                    LazyColumn(
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
                    LazyColumn(
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
                                onRetry = { viewModel.onEvent(AlbumDetailUIEvent.LoadAlbum(albumId)) }
                            )
                        }
                    }
                }

                album == null -> {
                    LazyColumn(
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
                            EmptySection(message = "Albüm bulunamadı")
                        }
                    }
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(DarkBackground),
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
                                    title = "Şarkılar",
                                    subtitle = "${uiState.tracks.size} şarkı"
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
                                        favoritesViewModel.onEvent(FavoritesUIEvent.ToggleFavorite(track))
                                    },
                                    onArtistClick = {
                                        onArtistClick(track.artist.id)
                                    }
                                )
                            }
                        } else {
                            item {
                                EmptySection(
                                    message = "Bu albümde henüz şarkı bulunamadı"
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

