package com.dilara.beatify.presentation.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.dilara.beatify.core.navigation.BeatifyRoutes
import com.dilara.beatify.presentation.state.PlayerUIEvent
import com.dilara.beatify.presentation.ui.components.BeatifyBottomNavigationBar
import com.dilara.beatify.presentation.ui.components.player.FullScreenPlayer
import com.dilara.beatify.presentation.ui.components.player.MiniPlayer
import com.dilara.beatify.presentation.ui.home.HomeScreen
import com.dilara.beatify.presentation.ui.search.SearchScreen
import com.dilara.beatify.presentation.viewmodel.PlayerViewModel

@Composable
fun BeatifyNavigation(
    navController: NavHostController = rememberNavController(),
    startDestination: String = BeatifyRoutes.Home.route
) {
    val playerViewModel: PlayerViewModel = hiltViewModel()
    val playerState by playerViewModel.uiState.collectAsState()
    
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val showBottomBar = currentRoute in listOf(
        BeatifyRoutes.Home.route,
        BeatifyRoutes.Search.route,
        BeatifyRoutes.Favorites.route,
        BeatifyRoutes.Playlists.route,
        BeatifyRoutes.Profile.route
    )

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                Column {
                    if (playerState.currentTrack != null) {
                        MiniPlayer(
                            track = playerState.currentTrack,
                            isPlaying = playerState.isPlaying,
                            onPlayPauseClick = {
                                playerViewModel.onEvent(PlayerUIEvent.PlayPause)
                            },
                            onExpandClick = {
                                playerViewModel.onEvent(PlayerUIEvent.Expand)
                            }
                        )
                    }
                    
                    BeatifyBottomNavigationBar(
                        items = BottomNavItems.items,
                        selectedRoute = currentRoute ?: BeatifyRoutes.Home.route,
                        onItemClick = { route ->
                            navController.navigate(route) {
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            NavHost(
                navController = navController,
                startDestination = startDestination
            ) {
                composable(route = BeatifyRoutes.Home.route) {
                    HomeScreen(
                        onTrackClick = { track ->
                            playerViewModel.onEvent(PlayerUIEvent.PlayTrack(track, emptyList()))
                        }
                    )
                }

                composable(route = BeatifyRoutes.Search.route) {
                    SearchScreen(
                        onTrackClick = { track ->
                            playerViewModel.onEvent(PlayerUIEvent.PlayTrack(track, emptyList()))
                        }
                    )
                }

                composable(route = BeatifyRoutes.Favorites.route) {
                    FavoritesPlaceholder()
                }

                composable(route = BeatifyRoutes.Playlists.route) {
                    PlaylistsPlaceholder()
                }

                composable(route = BeatifyRoutes.PlaylistDetail.route) {
                    PlaylistDetailPlaceholder()
                }

                composable(route = BeatifyRoutes.Profile.route) {
                    ProfilePlaceholder()
                }

                composable(route = BeatifyRoutes.TrackDetail.route) {
                    TrackDetailPlaceholder()
                }

                composable(route = BeatifyRoutes.ArtistDetail.route) {
                    ArtistDetailPlaceholder()
                }

                composable(route = BeatifyRoutes.AlbumDetail.route) {
                    AlbumDetailPlaceholder()
                }
            }
            
            if (playerState.isExpanded && playerState.currentTrack != null) {
                FullScreenPlayer(
                    track = playerState.currentTrack,
                    isPlaying = playerState.isPlaying,
                    position = playerState.position,
                    duration = playerState.duration,
                    repeatMode = playerState.repeatMode,
                    isShuffleEnabled = playerState.isShuffleEnabled,
                    error = playerState.error,
                    onPlayPauseClick = {
                        playerViewModel.onEvent(PlayerUIEvent.PlayPause)
                    },
                    onNextClick = {
                        playerViewModel.onEvent(PlayerUIEvent.Next)
                    },
                    onPreviousClick = {
                        playerViewModel.onEvent(PlayerUIEvent.Previous)
                    },
                    onSeekTo = { position ->
                        playerViewModel.onEvent(PlayerUIEvent.SeekTo(position))
                    },
                    onRepeatClick = {
                        playerViewModel.onEvent(PlayerUIEvent.ToggleRepeat)
                    },
                    onShuffleClick = {
                        playerViewModel.onEvent(PlayerUIEvent.ToggleShuffle)
                    },
                    onDismiss = {
                        playerViewModel.onEvent(PlayerUIEvent.Collapse)
                    }
                )
            }
        }
    }
}

@Composable
fun HomePlaceholder() {
    Text("Home Screen - Coming Soon")
}

@Composable
fun SearchPlaceholder() {
    Text("Search Screen - Coming Soon")
}

@Composable
fun FavoritesPlaceholder() {
    Text("Favorites Screen - Coming Soon")
}

@Composable
fun PlaylistsPlaceholder() {
    Text("Playlists Screen - Coming Soon")
}

@Composable
fun PlaylistDetailPlaceholder() {
    Text("Playlist Detail Screen - Coming Soon")
}

@Composable
fun ProfilePlaceholder() {
    Text("Profile Screen - Coming Soon")
}

@Composable
fun TrackDetailPlaceholder() {
    Text("Track Detail Screen - Coming Soon")
}

@Composable
fun ArtistDetailPlaceholder() {
    Text("Artist Detail Screen - Coming Soon")
}

@Composable
fun AlbumDetailPlaceholder() {
    Text("Album Detail Screen - Coming Soon")
}
