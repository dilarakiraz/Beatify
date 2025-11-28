package com.dilara.beatify.presentation.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.dilara.beatify.core.navigation.BeatifyRoutes
import com.dilara.beatify.presentation.ui.components.BeatifyBottomNavigationBar
import com.dilara.beatify.presentation.ui.home.HomeScreen
import com.dilara.beatify.presentation.ui.search.SearchScreen

@Composable
fun BeatifyNavigation(
    navController: NavHostController = rememberNavController(),
    startDestination: String = BeatifyRoutes.Home.route
) {
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
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(route = BeatifyRoutes.Home.route) {
                HomeScreen()
            }

            composable(route = BeatifyRoutes.Search.route) {
                SearchScreen()
            }

            composable(route = BeatifyRoutes.Favorites.route) {
                // TODO: Favorites Screen
                FavoritesPlaceholder()
            }

            composable(route = BeatifyRoutes.Playlists.route) {
                // TODO: Playlists Screen
                PlaylistsPlaceholder()
            }

            composable(route = BeatifyRoutes.PlaylistDetail.route) {
                // TODO: Playlist Detail Screen
                PlaylistDetailPlaceholder()
            }

            composable(route = BeatifyRoutes.Profile.route) {
                // TODO: Profile Screen
                ProfilePlaceholder()
            }

            composable(route = BeatifyRoutes.TrackDetail.route) {
                // TODO: Track Detail Screen
                TrackDetailPlaceholder()
            }

            composable(route = BeatifyRoutes.ArtistDetail.route) {
                // TODO: Artist Detail Screen
                ArtistDetailPlaceholder()
            }

            composable(route = BeatifyRoutes.AlbumDetail.route) {
                // TODO: Album Detail Screen
                AlbumDetailPlaceholder()
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
