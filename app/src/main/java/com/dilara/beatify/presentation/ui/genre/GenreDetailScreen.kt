package com.dilara.beatify.presentation.ui.genre

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import com.dilara.beatify.domain.model.Track
import com.dilara.beatify.presentation.state.GenreDetailUIEvent
import com.dilara.beatify.presentation.ui.components.ArtistCard
import com.dilara.beatify.presentation.ui.components.TrackCard
import com.dilara.beatify.presentation.ui.components.common.BackButton
import com.dilara.beatify.presentation.ui.components.common.HorizontalItemsList
import com.dilara.beatify.presentation.ui.components.common.SectionHeader
import com.dilara.beatify.presentation.ui.hooks.useFavoritesState
import com.dilara.beatify.presentation.viewmodel.GenreDetailViewModel
import com.dilara.beatify.ui.theme.LightPrimary
import com.dilara.beatify.ui.theme.LightTertiary
import com.dilara.beatify.ui.theme.NeonCyan
import com.dilara.beatify.ui.theme.NeonPurple
import com.dilara.beatify.ui.theme.isDarkTheme
import com.dilara.beatify.ui.theme.themeBackground
import com.dilara.beatify.ui.theme.themeTextPrimary

@Composable
fun GenreDetailScreen(
    genreId: Long,
    viewModel: GenreDetailViewModel = hiltViewModel(),
    onTrackClick: (Track, List<Track>) -> Unit = { _, _ -> },
    onArtistClick: (Long) -> Unit = {},
    onNavigateBack: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    val favoritesState = useFavoritesState()

    LaunchedEffect(genreId) {
        viewModel.onEvent(GenreDetailUIEvent.LoadGenre(genreId))
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(themeBackground)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                top = 176.dp,
                bottom = 160.dp
            ),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (uiState.isLoading && uiState.artists.isEmpty()) {
                items(
                    count = 5,
                    key = { index -> "loading_skeleton_$index" }
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(80.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(
                                if (isDarkTheme) {
                                    Color.White.copy(alpha = 0.05f)
                                } else {
                                    Color.Black.copy(alpha = 0.05f)
                                }
                            )
                    )
                }
            }

            if (uiState.error != null && uiState.artists.isEmpty() && uiState.radioTracks.isEmpty()) {
                item(key = "error_section") {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = uiState.error ?: "Bir hata oluştu",
                            color = themeTextPrimary,
                            fontSize = 16.sp
                        )
                    }
                }
            }

            if (!uiState.isLoading || uiState.artists.isNotEmpty() || uiState.radioTracks.isNotEmpty()) {
                if (uiState.artists.isNotEmpty()) {
                    item(key = "artists_header") {
                        SectionHeader(title = "Popüler Sanatçılar")
                    }

                    item(key = "artists_list") {
                        HorizontalItemsList(
                            items = uiState.artists.take(10),
                            key = { artist -> "artist_${artist.id}" }
                        ) { artist ->
                            ArtistCard(
                                artist = artist,
                                onClick = {
                                    viewModel.onEvent(GenreDetailUIEvent.OnArtistClick(artist.id))
                                    onArtistClick(artist.id)
                                },
                                size = 120.dp
                            )
                        }
                    }
                }

                if (uiState.radioTracks.isNotEmpty()) {
                    item(key = "radio_header") {
                        SectionHeader(title = "Radio Mix")
                    }

                    items(
                        items = uiState.radioTracks,
                        key = { track -> "radio_track_${track.id}" }
                    ) { track ->
                        TrackCard(
                            track = track,
                            onClick = {
                                viewModel.onEvent(GenreDetailUIEvent.OnTrackClick(track.id))
                                onTrackClick(track, uiState.radioTracks)
                            },
                            isFavorite = favoritesState.favoriteTrackIds.contains(track.id),
                            onFavoriteClick = {
                                favoritesState.toggleFavorite(track)
                            }
                        )
                    }
                }
            }
        }
        
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
                .zIndex(1f)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))
                    .background(themeBackground)
            )
            
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))
                    .background(
                        brush = Brush.verticalGradient(
                            colors = if (isDarkTheme) {
                                listOf(
                                    NeonPurple.copy(alpha = 0.08f),
                                    NeonCyan.copy(alpha = 0.05f),
                                    Color.Transparent
                                )
                            } else {
                                listOf(
                                    LightPrimary.copy(alpha = 0.1f),
                                    LightTertiary.copy(alpha = 0.06f),
                                    Color.Transparent
                                )
                            }
                        )
                    )
            )
            
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp)
                    .padding(top = 12.dp, bottom = 20.dp)
                    .statusBarsPadding(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                BackButton(onClick = onNavigateBack)
                
                Text(
                    text = uiState.genre?.name ?: "Genre",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = (-1).sp,
                    lineHeight = 36.sp,
                    color = if (isDarkTheme) {
                        Color.White
                    } else {
                        Color.Black.copy(alpha = 0.9f)
                    },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

