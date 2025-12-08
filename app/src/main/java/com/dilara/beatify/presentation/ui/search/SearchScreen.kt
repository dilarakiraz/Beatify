package com.dilara.beatify.presentation.ui.search

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dilara.beatify.presentation.state.SearchUIEvent
import com.dilara.beatify.presentation.state.SearchType
import com.dilara.beatify.presentation.ui.components.AlbumCard
import com.dilara.beatify.presentation.ui.components.ArtistCard
import com.dilara.beatify.presentation.ui.components.BeatifySearchBar
import com.dilara.beatify.presentation.ui.components.PlaylistCard
import com.dilara.beatify.presentation.ui.components.TrackCard
import com.dilara.beatify.presentation.ui.components.common.CircularSearchHistoryChip
import com.dilara.beatify.presentation.ui.components.common.EmptySection
import com.dilara.beatify.presentation.ui.components.common.ErrorSection
import com.dilara.beatify.presentation.ui.components.common.SectionHeader
import com.dilara.beatify.presentation.ui.components.common.LoadingSkeleton
import com.dilara.beatify.presentation.ui.hooks.useFavoritesState
import com.dilara.beatify.presentation.viewmodel.SearchViewModel
import com.dilara.beatify.ui.theme.DarkSurface
import com.dilara.beatify.ui.theme.LightSurface
import com.dilara.beatify.ui.theme.NeonCyan
import com.dilara.beatify.ui.theme.NeonPurple
import com.dilara.beatify.ui.theme.NeonTextPrimary
import com.dilara.beatify.ui.theme.NeonTextSecondary
import com.dilara.beatify.ui.theme.isDarkTheme
import com.dilara.beatify.ui.theme.themeBackground

@Composable
fun SearchScreen(
    viewModel: SearchViewModel = hiltViewModel(),
    onTrackClick: (com.dilara.beatify.domain.model.Track) -> Unit = {},
    onArtistClick: (Long) -> Unit = {},
    onAlbumClick: (Long) -> Unit = {},
    onPlaylistClick: (Long, String) -> Unit = { _, _ -> }
) {
    val uiState by viewModel.uiState.collectAsState()
    val favoritesState = useFavoritesState()
    
    val searchTypes = listOf(
        SearchType.ALL to "Tümü",
        SearchType.TRACKS to "Şarkılar",
        SearchType.ARTISTS to "Sanatçılar",
        SearchType.ALBUMS to "Albümler",
        SearchType.PLAYLISTS to "Playlistler"
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(themeBackground),
        contentPadding = PaddingValues(
            start = 16.dp,
            end = 16.dp,
            top = 0.dp,
            bottom = 8.dp
        ),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        stickyHeader {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(themeBackground)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
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
                
                if (uiState.searchQuery.isNotBlank()) {
                    LazyRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp)
                    ) {
                        items(
                            items = searchTypes,
                            key = { it.first }
                        ) { (type, label) ->
                            val isSelected = uiState.searchType == type
                            val scale by animateFloatAsState(
                                targetValue = if (isSelected) 1.05f else 1f,
                                animationSpec = tween(200),
                                label = "tab_scale"
                            )
                            
                            Box(
                                modifier = Modifier
                                    .scale(scale)
                                    .height(40.dp)
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(
                                        brush = if (isSelected) {
                                            Brush.horizontalGradient(
                                                colors = if (isDarkTheme) {
                                                    listOf(
                                                        NeonCyan.copy(alpha = 0.3f),
                                                        NeonPurple.copy(alpha = 0.25f)
                                                    )
                                                } else {
                                                    listOf(
                                                        Color(0xFF6366F1).copy(alpha = 0.15f),
                                                        Color(0xFF06B6D4).copy(alpha = 0.12f)
                                                    )
                                                }
                                            )
                                        } else {
                                            Brush.horizontalGradient(
                                                colors = listOf(
                                                    if (isDarkTheme) {
                                                        DarkSurface.copy(alpha = 0.6f)
                                                    } else {
                                                        LightSurface.copy(alpha = 0.8f)
                                                    },
                                                    if (isDarkTheme) {
                                                        DarkSurface.copy(alpha = 0.4f)
                                                    } else {
                                                        LightSurface.copy(alpha = 0.6f)
                                                    }
                                                )
                                            )
                                        }
                                    )
                                    .clickable {
                                        viewModel.onEvent(SearchUIEvent.OnSearchTypeChange(type))
                                    }
                                    .padding(horizontal = 20.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = label,
                                    fontSize = 14.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) {
                                        if (isDarkTheme) NeonTextPrimary else Color(0xFF18181B)
                                    } else {
                                        if (isDarkTheme) NeonTextSecondary else Color(0xFF71717A)
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }

        when {
            uiState.isLoading -> {
                items(5) {
                    LoadingSkeleton()
                }
            }

            uiState.error != null -> {
                item {
                    ErrorSection(
                        message = uiState.error ?: "Bilinmeyen hata",
                        onRetry = {
                            if (uiState.searchQuery.isNotBlank()) {
                                viewModel.onEvent(SearchUIEvent.OnQueryChange(uiState.searchQuery))
                            }
                        }
                    )
                }
            }

            uiState.searchQuery.isBlank() -> {
                if (uiState.isLoadingSuggestions) {
                    items(5) {
                        LoadingSkeleton()
                    }
                } else {
                    if (uiState.searchHistory.isNotEmpty()) {
                        item {
                            SectionHeader(
                                title = "Son Aramalarım"
                            )
                        }
                        
                        item {
                            LazyRow(
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                            ) {
                                items(
                                    items = uiState.searchHistory,
                                    key = { history -> history.id }
                                ) { history ->
                                    CircularSearchHistoryChip(
                                        track = history.track,
                                        onClick = {
                                            viewModel.onEvent(SearchUIEvent.OnTrackClick(history.track.id))
                                            onTrackClick(history.track)
                                        },
                                        onDelete = {
                                            viewModel.onEvent(SearchUIEvent.DeleteSearchHistory(history.id))
                                        }
                                    )
                                }
                            }
                        }
                        
                        item {
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                    
                    if (uiState.suggestedTracks.isNotEmpty()) {
                        item {
                            SectionHeader(
                                title = "Sizin İçin Önerilenler"
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
                                        onTrackClick(track)
                                    },
                                    isFavorite = favoritesState.favoriteTrackIds.contains(track.id),
                                    onFavoriteClick = {
                                        favoritesState.toggleFavorite(track)
                                    }
                                )
                            }
                        }
                    } else if (uiState.searchHistory.isEmpty()) {
                        item {
                            EmptySection(message = "Müzik aramak için yazmaya başlayın")
                        }
                    }
                }
            }

            uiState.tracks.isEmpty() && uiState.artists.isEmpty() && 
            uiState.albums.isEmpty() && uiState.playlists.isEmpty() -> {
                item {
                    EmptySection(message = "\"${uiState.searchQuery}\" için sonuç bulunamadı")
                }
            }

            else -> {
                when (uiState.searchType) {
                    SearchType.ALL -> {
                        if (uiState.artists.isNotEmpty()) {
                            item {
                                SectionHeader(
                                    title = "Sanatçılar",
                                    subtitle = "${uiState.artists.size} sanatçı bulundu"
                                )
                            }

                            item {
                                LazyRow(
                                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                                    contentPadding = PaddingValues(horizontal = 4.dp)
                                ) {
                                    items(
                                        items = uiState.artists,
                                        key = { artist -> artist.id }
                                    ) { artist ->
                                        ArtistCard(
                                            artist = artist,
                                            onClick = {
                                                viewModel.onEvent(SearchUIEvent.OnArtistClick(artist.id))
                                                onArtistClick(artist.id)
                                            },
                                            modifier = Modifier.size(width = 120.dp, height = 120.dp)
                                        )
                                    }
                                }
                            }
                        }

                        if (uiState.albums.isNotEmpty()) {
                            item {
                                SectionHeader(
                                    title = "Albümler",
                                    subtitle = "${uiState.albums.size} albüm bulundu"
                                )
                            }

                            item {
                                LazyRow(
                                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                                    contentPadding = PaddingValues(horizontal = 4.dp)
                                ) {
                                    items(
                                        items = uiState.albums,
                                        key = { album -> album.id }
                                    ) { album ->
                                        AlbumCard(
                                            album = album,
                                            onClick = {
                                                viewModel.onEvent(SearchUIEvent.OnAlbumClick(album.id))
                                                onAlbumClick(album.id)
                                            },
                                            modifier = Modifier.size(width = 160.dp, height = 120.dp)
                                        )
                                    }
                                }
                            }
                        }

                        if (uiState.playlists.isNotEmpty()) {
                            item {
                                SectionHeader(
                                    title = "Playlistler",
                                    subtitle = "${uiState.playlists.size} playlist bulundu"
                                )
                            }

                            item {
                                LazyRow(
                                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                                    contentPadding = PaddingValues(horizontal = 4.dp)
                                ) {
                                    items(
                                        items = uiState.playlists,
                                        key = { playlist -> playlist.id }
                                    ) { playlist ->
                                        PlaylistCard(
                                            playlist = playlist,
                                            onClick = {
                                                viewModel.onEvent(SearchUIEvent.OnPlaylistClick(playlist.id))
                                                onPlaylistClick(playlist.id, playlist.title)
                                            },
                                            modifier = Modifier.size(width = 160.dp, height = 120.dp)
                                        )
                                    }
                                }
                            }
                        }

                        if (uiState.tracks.isNotEmpty()) {
                            item {
                                SectionHeader(
                                    title = "Şarkılar",
                                    subtitle = "${uiState.tracks.size} şarkı bulundu"
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
                                            onTrackClick(track)
                                        },
                                        isFavorite = favoritesState.favoriteTrackIds.contains(track.id),
                                        onFavoriteClick = {
                                            favoritesState.toggleFavorite(track)
                                        },
                                        onArtistClick = {
                                            viewModel.onEvent(SearchUIEvent.OnArtistClick(track.artist.id))
                                            onArtistClick(track.artist.id)
                                        }
                                    )
                                }
                            }
                        }
                    }
                    SearchType.TRACKS -> {
                        if (uiState.tracks.isNotEmpty()) {
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
                                            onTrackClick(track)
                                        },
                                        isFavorite = favoritesState.favoriteTrackIds.contains(track.id),
                                        onFavoriteClick = {
                                            favoritesState.toggleFavorite(track)
                                        },
                                        onArtistClick = {
                                            viewModel.onEvent(SearchUIEvent.OnArtistClick(track.artist.id))
                                            onArtistClick(track.artist.id)
                                        }
                                    )
                                }
                            }
                        }
                    }
                    SearchType.ARTISTS -> {
                        if (uiState.artists.isNotEmpty()) {
                            item {
                                LazyRow(
                                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                                    contentPadding = PaddingValues(horizontal = 4.dp)
                                ) {
                                    items(
                                        items = uiState.artists,
                                        key = { artist -> artist.id }
                                    ) { artist ->
                                        ArtistCard(
                                            artist = artist,
                                            onClick = {
                                                viewModel.onEvent(SearchUIEvent.OnArtistClick(artist.id))
                                                onArtistClick(artist.id)
                                            },
                                            modifier = Modifier.size(width = 120.dp, height = 120.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                    SearchType.ALBUMS -> {
                        if (uiState.albums.isNotEmpty()) {
                            item {
                                LazyRow(
                                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                                    contentPadding = PaddingValues(horizontal = 4.dp)
                                ) {
                                    items(
                                        items = uiState.albums,
                                        key = { album -> album.id }
                                    ) { album ->
                                        AlbumCard(
                                            album = album,
                                            onClick = {
                                                viewModel.onEvent(SearchUIEvent.OnAlbumClick(album.id))
                                                onAlbumClick(album.id)
                                            },
                                            modifier = Modifier.size(width = 160.dp, height = 120.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                    SearchType.PLAYLISTS -> {
                        if (uiState.playlists.isNotEmpty()) {
                            item {
                                LazyRow(
                                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                                    contentPadding = PaddingValues(horizontal = 4.dp)
                                ) {
                                    items(
                                        items = uiState.playlists,
                                        key = { playlist -> playlist.id }
                                    ) { playlist ->
                                        PlaylistCard(
                                            playlist = playlist,
                                            onClick = {
                                                viewModel.onEvent(SearchUIEvent.OnPlaylistClick(playlist.id))
                                                onPlaylistClick(playlist.id, playlist.title)
                                            },
                                            modifier = Modifier.size(width = 160.dp, height = 120.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

