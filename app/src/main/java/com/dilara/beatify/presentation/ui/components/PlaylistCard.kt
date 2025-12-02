package com.dilara.beatify.presentation.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import coil.compose.AsyncImage
import com.dilara.beatify.domain.model.Playlist
import com.dilara.beatify.presentation.ui.components.common.GlassIconButton
import com.dilara.beatify.presentation.ui.components.common.GlassIconButtonStyle
import com.dilara.beatify.ui.theme.DarkSurface
import com.dilara.beatify.ui.theme.LightSurface
import com.dilara.beatify.ui.theme.NeonCyan
import com.dilara.beatify.ui.theme.isDarkTheme

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PlaylistCard(
    playlist: Playlist,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    onDelete: (() -> Unit)? = null
) {
    var showDeleteButton by remember { mutableStateOf(false) }
    
    val cardColor = if (isDarkTheme) {
        DarkSurface.copy(alpha = 0.6f)
    } else {
        LightSurface.copy(alpha = 0.95f)
    }
    
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .combinedClickable(
                onClick = onClick,
                onLongClick = {
                    if (onDelete != null) {
                        showDeleteButton = !showDeleteButton
                    }
                }
            ),
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
            if (playlist.coverUrl != null) {
                AsyncImage(
                    model = playlist.coverUrl,
                    contentDescription = playlist.name,
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
            
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.7f)
                            )
                        )
                    )
            )
            
            // Glass icon container (like FAB)
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(12.dp)
                    .size(48.dp)
            ) {
                // Glow effect
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            brush = if (isDarkTheme) {
                                Brush.radialGradient(
                                    colors = listOf(
                                        NeonCyan.copy(alpha = 0.3f),
                                        com.dilara.beatify.ui.theme.NeonPurple.copy(alpha = 0.2f),
                                        Color.Transparent
                                    )
                                )
                            } else {
                                Brush.radialGradient(
                                    colors = listOf(
                                        com.dilara.beatify.ui.theme.LightPrimary.copy(alpha = 0.4f),
                                        com.dilara.beatify.ui.theme.LightTertiary.copy(alpha = 0.3f),
                                        Color.Transparent
                                    )
                                )
                            }
                        )
                )
                
                // Glass container
                Box(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(40.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(
                            brush = if (isDarkTheme) {
                                Brush.linearGradient(
                                    colors = listOf(
                                        Color.White.copy(alpha = 0.08f),
                                        Color.White.copy(alpha = 0.04f)
                                    )
                                )
                            } else {
                                Brush.linearGradient(
                                    colors = listOf(
                                        Color.White.copy(alpha = 0.95f),
                                        Color.White.copy(alpha = 0.85f)
                                    )
                                )
                            }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.List,
                        contentDescription = "Playlist",
                        tint = if (isDarkTheme) Color.White else com.dilara.beatify.ui.theme.LightPrimary,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
            
            if (showDeleteButton && onDelete != null) {
                GlassIconButton(
                    icon = Icons.Default.Delete,
                    onClick = {
                        onDelete?.invoke()
                        showDeleteButton = false
                    },
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(12.dp),
                    style = GlassIconButtonStyle.DANGER,
                    contentDescription = "Sil"
                )
            }
            
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = playlist.name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,  // Her iki temada da beyaz (overlay üzerinde)
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

