package com.dilara.beatify.presentation.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.dilara.beatify.domain.model.Track
import com.dilara.beatify.presentation.ui.components.common.FavoriteButton
import com.dilara.beatify.ui.theme.BeatifyGradients
import com.dilara.beatify.ui.theme.DarkSurface
import com.dilara.beatify.ui.theme.LightSurface
import com.dilara.beatify.ui.theme.NeonPink
import com.dilara.beatify.ui.theme.isDarkTheme
import com.dilara.beatify.ui.theme.themeTextPrimary
import com.dilara.beatify.ui.theme.themeTextSecondary

enum class TrackCardStyle {
    VERTICAL,
    HORIZONTAL
}

@Composable
fun TrackCard(
    track: Track,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    style: TrackCardStyle = TrackCardStyle.VERTICAL,
    isFavorite: Boolean = false,
    onFavoriteClick: (() -> Unit)? = null,
    onDelete: (() -> Unit)? = null,
    onArtistClick: (() -> Unit)? = null,
    isDragging: Boolean = false
) {
    val deleteInteractionSource = remember { MutableInteractionSource() }
    val isDeletePressed by deleteInteractionSource.collectIsPressedAsState()
    
    val config = when (style) {
        TrackCardStyle.VERTICAL -> TrackCardConfig(
            cornerRadius = 16.dp,
            cardAlpha = 0.6f,
            padding = 12.dp,
            spacing = 12.dp,
            imageSize = 56.dp,
            imageCornerRadius = 12.dp,
            titleFontSize = 16.sp,
            artistFontSize = 13.sp,
            durationFontSize = 12.sp,
            showAlbumTitle = false,
            gradientType = GradientType.LINEAR  // LINEAR daha belirgin
        )
        TrackCardStyle.HORIZONTAL -> TrackCardConfig(
            cornerRadius = 12.dp,
            cardAlpha = 0.7f,
            padding = 8.dp,
            spacing = 8.dp,
            imageSize = 48.dp,
            imageCornerRadius = 10.dp,
            titleFontSize = 15.sp,
            artistFontSize = 13.sp,
            durationFontSize = 12.sp,
            showAlbumTitle = true,
            gradientType = GradientType.LINEAR
        )
    }

    val cardColor = if (isDarkTheme) {
        DarkSurface.copy(alpha = config.cardAlpha)
    } else {
        LightSurface.copy(alpha = 0.95f)  // Açık tema: neredeyse opak beyaz
    }
    
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(config.cornerRadius)),
        shape = RoundedCornerShape(config.cornerRadius),
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
                .background(
                    brush = when (config.gradientType) {
                        GradientType.HORIZONTAL -> BeatifyGradients.cardGradientHorizontal(isDarkTheme)
                        GradientType.LINEAR -> BeatifyGradients.cardGradientLinear(isDarkTheme)
                    }
                )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .then(
                        if (!isDragging) {
                            Modifier.clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) { onClick() }
                        } else {
                            Modifier
                        }
                    )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(config.padding),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(config.spacing)
                ) {
                    AsyncImage(
                        model = when (style) {
                            TrackCardStyle.HORIZONTAL -> track.album.coverBig ?: track.album.coverMedium ?: track.album.cover
                            TrackCardStyle.VERTICAL -> track.album.coverMedium ?: track.album.cover
                        },
                        contentDescription = track.album.title,
                        modifier = Modifier
                            .size(config.imageSize)
                            .clip(RoundedCornerShape(config.imageCornerRadius)),
                        contentScale = ContentScale.Crop
                    )

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .then(
                                if (style == TrackCardStyle.VERTICAL) {
                                    Modifier.fillMaxHeight()
                                } else {
                                    Modifier
                                }
                            )
                            .padding(end = if (onFavoriteClick != null) 44.dp else 0.dp),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = track.title,
                            fontSize = config.titleFontSize,
                            fontWeight = FontWeight.Bold,
                            color = themeTextPrimary,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                        
                        Spacer(modifier = Modifier.height(if (style == TrackCardStyle.VERTICAL) 4.dp else 6.dp))
                        
                        Text(
                            text = track.artist.name,
                            fontSize = config.artistFontSize,
                            fontWeight = FontWeight.Normal,
                            color = themeTextSecondary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier
                                .then(
                                    if (onArtistClick != null) {
                                        Modifier.clickable(
                                            onClick = onArtistClick,
                                            indication = null,
                                            interactionSource = remember { MutableInteractionSource() }
                                        )
                                    } else {
                                        Modifier
                                    }
                                )
                        )
                        
                        if (config.showAlbumTitle) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = track.album.title,
                                fontSize = 12.sp,
                                color = themeTextSecondary.copy(alpha = 0.7f),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }

                    if (onFavoriteClick == null && onDelete == null) {
                        Text(
                            text = formatDuration(track.duration),
                            fontSize = config.durationFontSize,
                            color = themeTextSecondary,
                            fontWeight = if (style == TrackCardStyle.HORIZONTAL) FontWeight.Medium else FontWeight.Normal,
                            modifier = if (style == TrackCardStyle.VERTICAL) Modifier.padding(end = 8.dp) else Modifier
                        )
                    }
                }
            }

            // Favorite button - sağ üst köşe
            if (onFavoriteClick != null) {
                FavoriteButton(
                    isFavorite = isFavorite,
                    onClick = onFavoriteClick,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .offset(x = (-4).dp, y = 4.dp),
                    size = 36.dp,
                    iconSize = 20.dp
                )
            }
            
            if (onDelete != null && onFavoriteClick == null) {
                val deleteButtonScale by animateFloatAsState(
                    targetValue = if (isDeletePressed) 1.15f else 1f,
                    animationSpec = tween(200),
                    label = "delete_scale"
                )
                
                val deleteButtonAlpha by animateFloatAsState(
                    targetValue = if (isDeletePressed) 1f else 0.85f,
                    animationSpec = tween(200),
                    label = "delete_alpha"
                )
                
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(end = 12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .graphicsLayer {
                                scaleX = deleteButtonScale
                                scaleY = deleteButtonScale
                                alpha = deleteButtonAlpha
                            }
                            .background(
                                brush = Brush.radialGradient(
                                    colors = listOf(
                                        NeonPink.copy(alpha = 0.3f),
                                        NeonPink.copy(alpha = 0.15f),
                                        Transparent
                                    ),
                                    radius = 60f
                                ),
                                shape = CircleShape
                            )
                            .clickable(
                                onClick = { onDelete() },
                                interactionSource = deleteInteractionSource,
                                indication = null
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .background(
                                    brush = Brush.radialGradient(
                                        colors = listOf(
                                            NeonPink.copy(alpha = if (isDeletePressed) 0.4f else 0.25f),
                                            NeonPink.copy(alpha = if (isDeletePressed) 0.2f else 0.12f)
                                        )
                                    ),
                                    shape = CircleShape
                                )
                                .clip(CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Sil",
                                tint = NeonPink.copy(alpha = if (isDeletePressed) 1f else 0.9f),
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TrackCardHorizontal(
    track: Track,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isFavorite: Boolean = false,
    onFavoriteClick: (() -> Unit)? = null
) {
    TrackCard(
        track = track,
        onClick = onClick,
        modifier = modifier,
        style = TrackCardStyle.HORIZONTAL,
        isFavorite = isFavorite,
        onFavoriteClick = onFavoriteClick
    )
}

private data class TrackCardConfig(
    val cornerRadius: Dp,
    val cardAlpha: Float,
    val padding: Dp,
    val spacing: Dp,
    val imageSize: Dp,
    val imageCornerRadius: Dp,
    val titleFontSize: androidx.compose.ui.unit.TextUnit,
    val artistFontSize: androidx.compose.ui.unit.TextUnit,
    val durationFontSize: androidx.compose.ui.unit.TextUnit,
    val showAlbumTitle: Boolean,
    val gradientType: GradientType
)

private enum class GradientType {
    HORIZONTAL,
    LINEAR
}

private fun formatDuration(seconds: Int): String {
    val minutes = seconds / 60
    val secs = seconds % 60
    return String.format("%d:%02d", minutes, secs)
}
