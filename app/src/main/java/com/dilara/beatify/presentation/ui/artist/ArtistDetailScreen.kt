package com.dilara.beatify.presentation.ui.artist

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.dilara.beatify.domain.model.Track
import com.dilara.beatify.presentation.state.ArtistDetailUIEvent
import com.dilara.beatify.presentation.ui.components.AlbumCard
import com.dilara.beatify.presentation.ui.components.TrackCard
import com.dilara.beatify.presentation.ui.components.common.DetailScreenContent
import com.dilara.beatify.presentation.ui.components.common.DetailScreenHeader
import com.dilara.beatify.presentation.ui.components.common.EmptySection
import com.dilara.beatify.presentation.ui.components.common.HorizontalItemsList
import com.dilara.beatify.presentation.ui.components.common.SectionHeader
import com.dilara.beatify.presentation.ui.hooks.useFavoritesState
import com.dilara.beatify.presentation.viewmodel.ArtistDetailViewModel
import com.dilara.beatify.ui.theme.DarkSurface
import com.dilara.beatify.ui.theme.NeonTextPrimary
import com.dilara.beatify.ui.theme.themeBackground

@Composable
fun ArtistDetailScreen(
    artistId: Long,
    viewModel: ArtistDetailViewModel = hiltViewModel(),
    onTrackClick: (Track, List<Track>) -> Unit = { _, _ -> },
    onAlbumClick: (Long) -> Unit = {},
    onArtistClick: (Long) -> Unit = {},
    onNavigateBack: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    val favoritesState = useFavoritesState()

    LaunchedEffect(artistId) {
        viewModel.onEvent(ArtistDetailUIEvent.LoadArtist(artistId))
    }

    Box(modifier = Modifier.fillMaxSize()) {
        val artist = uiState.artist
        
        DetailScreenHeader(
            imageUrl = artist?.pictureXl ?: artist?.pictureBig ?: artist?.pictureMedium,
            title = artist?.name ?: "",
            subtitle = if (uiState.topTracks.isNotEmpty()) "${uiState.topTracks.size} popüler şarkı" else null,
            onNavigateBack = onNavigateBack
        )

        DetailScreenContent(
            isLoading = uiState.isLoading,
            error = uiState.error,
            isEmpty = artist == null && !uiState.isLoading && uiState.error == null,
            emptyMessage = "Sanatçı bulunamadı",
            onRetry = { viewModel.onEvent(ArtistDetailUIEvent.LoadArtist(artistId)) },
            modifier = Modifier.fillMaxSize()
        ) {
            if (artist != null) {
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
                        if (uiState.topTracks.isNotEmpty()) {
                            item {
                                SectionHeader(
                                    title = "Popüler Şarkılar",
                                )
                            }
                            
                            items(
                                items = uiState.topTracks,
                                key = { track -> track.id }
                            ) { track ->
                                TrackCard(
                                    track = track,
                                    onClick = {
                                        onTrackClick(track, uiState.topTracks)
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
                        }

                        item {
                            SectionHeader(
                                title = "Albümler",
                                subtitle = if (uiState.albums.isNotEmpty()) "${uiState.albums.size} albüm" else "Albüm bulunamadı"
                            )
                        }

                        if (uiState.albums.isNotEmpty()) {
                            item {
                                HorizontalItemsList(
                                    items = uiState.albums,
                                    key = { album -> album.id }
                                ) { album ->
                                    AlbumCard(
                                        album = album,
                                        onClick = { onAlbumClick(album.id) },
                                        modifier = Modifier.size(width = 160.dp, height = 120.dp)
                                    )
                                }
                            }
                        } else {
                            item {
                                EmptySection(
                                    message = "Bu sanatçının albümü bulunamadı"
                                )
                            }
                        }

                        if (uiState.relatedArtists.isNotEmpty()) {
                            item {
                                SectionHeader(
                                    title = "İlgili Sanatçılar",
                                    subtitle = "${uiState.relatedArtists.size} sanatçı"
                                )
                            }

                            item {
                                HorizontalItemsList(
                                    items = uiState.relatedArtists,
                                    key = { artist -> artist.id }
                                ) { relatedArtist ->
                                    RelatedArtistCard(
                                        artist = relatedArtist,
                                        onClick = { onArtistClick(relatedArtist.id) }
                                    )
                                }
                            }
                        }

                        if (uiState.topTracks.isEmpty() && uiState.albums.isEmpty() && uiState.relatedArtists.isEmpty()) {
                            item {
                                EmptySection(
                                    message = "Bu sanatçı hakkında henüz içerik bulunamadı"
                                )
                            }
                        }
                }
            }
        }
    }
}

@Composable
private fun RelatedArtistCard(
    artist: com.dilara.beatify.domain.model.Artist,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(120.dp)
            .clip(CircleShape)
            .background(DarkSurface.copy(alpha = 0.6f))
            .clickable(onClick = onClick)
    ) {
        if (artist.pictureBig != null || artist.pictureMedium != null) {
            AsyncImage(
                model = artist.pictureBig ?: artist.pictureMedium,
                contentDescription = artist.name,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.5f)
                        )
                    )
                )
        )

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = artist.name,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = NeonTextPrimary,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }
}

