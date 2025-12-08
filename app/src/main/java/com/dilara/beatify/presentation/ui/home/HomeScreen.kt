package com.dilara.beatify.presentation.ui.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dilara.beatify.presentation.state.HomeUIEvent
import com.dilara.beatify.presentation.ui.components.AlbumCard
import com.dilara.beatify.presentation.ui.components.ArtistCard
import com.dilara.beatify.presentation.ui.components.GenreCard
import com.dilara.beatify.presentation.ui.components.RadioCard
import com.dilara.beatify.presentation.ui.components.PlaylistCard
import com.dilara.beatify.presentation.ui.components.RecentTrackCard
import com.dilara.beatify.presentation.ui.components.TrackCard
import com.dilara.beatify.presentation.ui.components.TrackCardHorizontal
import com.dilara.beatify.presentation.ui.components.common.EmptySection
import com.dilara.beatify.presentation.ui.components.common.ErrorSection
import com.dilara.beatify.presentation.ui.components.common.HorizontalItemsList
import com.dilara.beatify.presentation.ui.components.common.SectionHeader
import com.dilara.beatify.presentation.ui.components.common.LoadingSkeleton
import com.dilara.beatify.presentation.ui.components.profile.ProfileButton
import com.dilara.beatify.presentation.ui.components.profile.ProfileDrawerContent
import com.dilara.beatify.presentation.ui.hooks.useFavoritesState
import com.dilara.beatify.presentation.viewmodel.HomeViewModel
import com.dilara.beatify.R
import com.dilara.beatify.core.utils.stringResource
import com.dilara.beatify.ui.theme.themeBackground
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onTrackClick: (com.dilara.beatify.domain.model.Track) -> Unit = {},
    onArtistClick: (Long) -> Unit = {},
    onAlbumClick: (Long) -> Unit = {},
    onGenreClick: (Long) -> Unit = {},
    onRadioClick: (Long, String) -> Unit = { _, _ -> },
    onPlaylistClick: (Long, String) -> Unit = { _, _ -> }
) {
    val uiState by viewModel.uiState.collectAsState()
    val favoritesState = useFavoritesState()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val profileImageUri: String? = null

    val density = LocalDensity.current

    BoxWithConstraints {
        val drawerWidth: Dp = with(density) {
            (maxWidth.toPx() * 0.50f).toDp()
        }

        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                ProfileDrawerContent(
                    profileImageUri = profileImageUri,
                    onDismiss = { scope.launch { drawerState.close() } },
                    modifier = Modifier.width(drawerWidth)
                )
            }
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(themeBackground),
                contentPadding = PaddingValues(
                    horizontal = 16.dp,
                    vertical = 8.dp
                ),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        ProfileButton(
                            profileImageUri = profileImageUri,
                            onClick = { scope.launch { drawerState.open() } }
                        )

                        SectionHeader(
                            title = stringResource(R.string.welcome),
                        )
                    }
                }

                if (!uiState.isLoading && uiState.error == null && uiState.topTracks.isNotEmpty()) {
                    item {
                        SectionHeader(
                            title = stringResource(R.string.featured),
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
                                },
                                isFavorite = favoritesState.favoriteTrackIds.contains(track.id),
                                onFavoriteClick = {
                                    favoritesState.toggleFavorite(track)
                                }
                            )
                        }
                    }
                }

                if (uiState.dailyMix.isNotEmpty()) {
                    item {
                        SectionHeader(
                            title = stringResource(R.string.daily_mix),
                        )
                    }

                    item {
                        HorizontalItemsList(
                            items = uiState.dailyMix.take(10),
                            key = { track -> track.id }
                        ) { track ->
                            RecentTrackCard(
                                track = track,
                                onClick = {
                                    viewModel.onEvent(HomeUIEvent.OnTrackClick(track.id))
                                    onTrackClick(track)
                                },
                                isFavorite = favoritesState.favoriteTrackIds.contains(track.id),
                                onFavoriteClick = {
                                    favoritesState.toggleFavorite(track)
                                }
                            )
                        }
                    }
                }

                if (uiState.topArtists.isNotEmpty()) {
                    item {
                        SectionHeader(
                            title = stringResource(R.string.top_artists),
                        )
                    }

                    item {
                        HorizontalItemsList(
                            items = uiState.topArtists.take(10),
                            key = { artist -> artist.id }
                        ) { artist ->
                            ArtistCard(
                                artist = artist,
                                onClick = {
                                    viewModel.onEvent(HomeUIEvent.OnArtistClick(artist.id))
                                    onArtistClick(artist.id)
                                },
                                size = 120.dp
                            )
                        }
                    }
                }

                if (uiState.topAlbums.isNotEmpty()) {
                    item {
                        SectionHeader(
                            title = stringResource(R.string.top_albums),
                        )
                    }

                    item {
                        HorizontalItemsList(
                            items = uiState.topAlbums.take(10),
                            key = { album -> album.id }
                        ) { album ->
                            AlbumCard(
                                album = album,
                                onClick = {
                                    viewModel.onEvent(HomeUIEvent.OnAlbumClick(album.id))
                                    onAlbumClick(album.id)
                                },
                                modifier = Modifier.size(width = 160.dp, height = 120.dp)
                            )
                        }
                    }
                }

                if (uiState.genres.isNotEmpty()) {
                    item {
                        SectionHeader(
                            title = stringResource(R.string.music_genres),
                        )
                    }

                    item {
                        HorizontalItemsList(
                            items = uiState.genres.take(10),
                            key = { genre -> genre.id }
                        ) { genre ->
                            GenreCard(
                                genre = genre,
                                onClick = {
                                    viewModel.onEvent(HomeUIEvent.OnGenreClick(genre.id))
                                    onGenreClick(genre.id)
                                }
                            )
                        }
                    }
                }

                if (uiState.radios.isNotEmpty()) {
                    item {
                        SectionHeader(
                            title = stringResource(R.string.radios),
                        )
                    }

                    item {
                        HorizontalItemsList(
                            items = uiState.radios.take(10),
                            key = { radio -> radio.id }
                        ) { radio ->
                            RadioCard(
                                radio = radio,
                                onClick = {
                                    viewModel.onEvent(HomeUIEvent.OnRadioClick(radio.id))
                                    onRadioClick(radio.id, radio.title)
                                }
                            )
                        }
                    }
                }

                if (uiState.topPlaylists.isNotEmpty()) {
                    item {
                        SectionHeader(
                            title = stringResource(R.string.top_playlists),
                        )
                    }

                    item {
                        HorizontalItemsList(
                            items = uiState.topPlaylists.take(10),
                            key = { playlist -> playlist.id }
                        ) { playlist ->
                            PlaylistCard(
                                playlist = playlist,
                                onClick = {
                                    viewModel.onEvent(HomeUIEvent.OnPlaylistClick(playlist.id))
                                    onPlaylistClick(playlist.id, playlist.title)
                                }
                            )
                        }
                    }
                }

                if (uiState.recentTracks.isNotEmpty()) {
                    item {
                        SectionHeader(
                            title = stringResource(R.string.recently_played),
                        )
                    }

                    item {
                        HorizontalItemsList(
                            items = uiState.recentTracks.take(10),
                            key = { track -> track.id }
                        ) { track ->
                            RecentTrackCard(
                                track = track,
                                onClick = {
                                    viewModel.onEvent(HomeUIEvent.OnTrackClick(track.id))
                                    onTrackClick(track)
                                },
                                isFavorite = favoritesState.favoriteTrackIds.contains(track.id),
                                onFavoriteClick = {
                                    favoritesState.toggleFavorite(track)
                                }
                            )
                        }
                    }
                }

                item {
                    SectionHeader(
                        title = stringResource(R.string.top_tracks),
                    )
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
                                message = uiState.error ?: stringResource(R.string.error_unknown_en),
                                onRetry = { viewModel.onEvent(HomeUIEvent.Retry) }
                            )
                        }
                    }

                    uiState.topTracks.isEmpty() -> {
                        item {
                            EmptySection(message = stringResource(R.string.error_no_tracks))
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
            }
        }
    }
}

