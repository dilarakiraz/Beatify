package com.dilara.beatify.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.dilara.beatify.domain.model.PublicPlaylist
import com.dilara.beatify.ui.theme.DarkSurface
import com.dilara.beatify.ui.theme.LightSurface
import com.dilara.beatify.ui.theme.isDarkTheme

@Composable
fun PlaylistCard(
    playlist: PublicPlaylist,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val cardColor = if (isDarkTheme) {
        DarkSurface.copy(alpha = 0.6f)
    } else {
        LightSurface.copy(alpha = 0.95f)
    }
    
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = cardColor
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp,
            pressedElevation = 0.dp,
            hoveredElevation = 0.dp,
            focusedElevation = 0.dp
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
        ) {
            if (playlist.coverBig != null || playlist.coverMedium != null || playlist.cover != null) {
                AsyncImage(
                    model = playlist.coverBig ?: playlist.coverMedium ?: playlist.cover,
                    contentDescription = playlist.title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    contentScale = ContentScale.Crop
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .background(
                            if (isDarkTheme) DarkSurface.copy(alpha = 0.5f)
                            else LightSurface.copy(alpha = 0.8f)
                        )
                )
            }
            
        }
    }
}
