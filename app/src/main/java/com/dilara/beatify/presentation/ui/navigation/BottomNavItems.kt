package com.dilara.beatify.presentation.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import com.dilara.beatify.core.navigation.BeatifyRoutes
import com.dilara.beatify.presentation.ui.navigation.model.BottomNavItem

object BottomNavItems {
    val items = listOf(
        BottomNavItem(
            label = "Home",
            icon = Icons.Default.Home,
            selectedIcon = Icons.Default.Home,
            route = BeatifyRoutes.Home.route
        ),
        BottomNavItem(
            label = "Search",
            icon = Icons.Default.Search,
            selectedIcon = Icons.Default.Search,
            route = BeatifyRoutes.Search.route
        ),
        BottomNavItem(
            label = "Favorites",
            icon = Icons.Default.FavoriteBorder,
            selectedIcon = Icons.Default.Favorite,
            route = BeatifyRoutes.Favorites.route
        ),
        BottomNavItem(
            label = "Playlists",
            icon = Icons.Default.List,
            selectedIcon = Icons.Default.List,
            route = BeatifyRoutes.Playlists.route
        )
    )
}

