package com.dilara.beatify.presentation.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import com.dilara.beatify.core.navigation.BeatifyRoutes
import com.dilara.beatify.core.utils.stringResource
import com.dilara.beatify.R
import com.dilara.beatify.presentation.ui.navigation.model.BottomNavItem

object BottomNavItems {
    @Composable
    fun getItems() = listOf(
        BottomNavItem(
            label = stringResource(R.string.nav_home),
            icon = Icons.Default.Home,
            selectedIcon = Icons.Default.Home,
            route = BeatifyRoutes.Home.route
        ),
        BottomNavItem(
            label = stringResource(R.string.nav_search),
            icon = Icons.Default.Search,
            selectedIcon = Icons.Default.Search,
            route = BeatifyRoutes.Search.route
        ),
        BottomNavItem(
            label = stringResource(R.string.nav_favorites),
            icon = Icons.Default.FavoriteBorder,
            selectedIcon = Icons.Default.Favorite,
            route = BeatifyRoutes.Favorites.route
        ),
        BottomNavItem(
            label = stringResource(R.string.nav_playlists),
            icon = Icons.Default.List,
            selectedIcon = Icons.Default.List,
            route = BeatifyRoutes.Playlists.route
        )
    )
}

