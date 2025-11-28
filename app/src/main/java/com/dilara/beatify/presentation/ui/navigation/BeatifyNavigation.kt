package com.dilara.beatify.presentation.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.dilara.beatify.core.navigation.BeatifyRoutes
import com.dilara.beatify.core.navigation.NavigationAnimations
import com.dilara.beatify.presentation.state.FavoritesUIEvent
import com.dilara.beatify.presentation.state.PlayerUIEvent
import com.dilara.beatify.presentation.ui.components.BeatifyBottomNavigationBar
import com.dilara.beatify.presentation.ui.components.player.FullScreenPlayer
import com.dilara.beatify.presentation.ui.components.player.MiniPlayer
import com.dilara.beatify.presentation.ui.favorites.FavoritesScreen
import com.dilara.beatify.presentation.ui.home.HomeScreen
import com.dilara.beatify.presentation.ui.search.SearchScreen
import com.dilara.beatify.presentation.viewmodel.FavoritesViewModel
import com.dilara.beatify.presentation.viewmodel.PlayerViewModel

@Composable
fun BeatifyNavigation(
    navController: NavHostController = rememberNavController(),
    startDestination: String = BeatifyRoutes.Home.route
) {
    val playerViewModel: PlayerViewModel = hiltViewModel()
    val playerState by playerViewModel.uiState.collectAsState()
    
    val favoritesViewModel: FavoritesViewModel = hiltViewModel()
    
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    
    // Check if current track is favorite
    val currentTrackId = playerState.currentTrack?.id
    var isCurrentTrackFavorite by remember { mutableStateOf(false) }
    
    LaunchedEffect(currentTrackId) {
        if (currentTrackId != null) {
            favoritesViewModel.isFavorite(currentTrackId)
                .collect { isFavorite ->
                    isCurrentTrackFavorite = isFavorite
                }
        } else {
            isCurrentTrackFavorite = false
        }
    }

    val showBottomBar = currentRoute in listOf(
        BeatifyRoutes.Home.route,
        BeatifyRoutes.Search.route,
        BeatifyRoutes.Favorites.route,
        BeatifyRoutes.Playlists.route
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
                composable(
                    route = BeatifyRoutes.Home.route,
                    enterTransition = NavigationAnimations.homeScreenTransitions().first,
                    exitTransition = NavigationAnimations.homeScreenTransitions().second,
                    popEnterTransition = NavigationAnimations.homeScreenPopTransitions().first,
                    popExitTransition = NavigationAnimations.homeScreenPopTransitions().second
                ) {
                    HomeScreen(
                        onTrackClick = { track ->
                            playerViewModel.onEvent(PlayerUIEvent.PlayTrack(track, emptyList()))
                        }
                    )
                }

                composable(
                    route = BeatifyRoutes.Search.route,
                    enterTransition = NavigationAnimations.bottomNavScreenTransitions().first,
                    exitTransition = NavigationAnimations.bottomNavScreenTransitions().second,
                    popEnterTransition = NavigationAnimations.bottomNavScreenPopTransitions().first,
                    popExitTransition = NavigationAnimations.bottomNavScreenPopTransitions().second
                ) {
                    SearchScreen(
                        onTrackClick = { track ->
                            playerViewModel.onEvent(PlayerUIEvent.PlayTrack(track, emptyList()))
                        }
                    )
                }

                composable(
                    route = BeatifyRoutes.Favorites.route,
                    enterTransition = NavigationAnimations.bottomNavScreenTransitions().first,
                    exitTransition = NavigationAnimations.bottomNavScreenTransitions().second,
                    popEnterTransition = NavigationAnimations.bottomNavScreenPopTransitions().first,
                    popExitTransition = NavigationAnimations.bottomNavScreenPopTransitions().second
                ) {
                    FavoritesScreen(
                        onTrackClick = { track ->
                            playerViewModel.onEvent(PlayerUIEvent.PlayTrack(track, emptyList()))
                        }
                    )
                }

                composable(
                    route = BeatifyRoutes.Playlists.route,
                    enterTransition = NavigationAnimations.bottomNavScreenTransitions().first,
                    exitTransition = NavigationAnimations.bottomNavScreenTransitions().second,
                    popEnterTransition = NavigationAnimations.bottomNavScreenPopTransitions().first,
                    popExitTransition = NavigationAnimations.bottomNavScreenPopTransitions().second
                ) {
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
                    isFavorite = if (currentTrackId != null) isCurrentTrackFavorite else false,
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
                    onFavoriteClick = {
                        playerState.currentTrack?.let { track ->
                            favoritesViewModel.onEvent(FavoritesUIEvent.ToggleFavorite(track))
                        }
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
