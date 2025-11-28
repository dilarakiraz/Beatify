package com.dilara.beatify.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.dilara.beatify.domain.model.Track
import com.dilara.beatify.presentation.ui.components.common.FavoriteButton
import com.dilara.beatify.ui.theme.DarkSurface
import com.dilara.beatify.ui.theme.NeonCyan
import com.dilara.beatify.ui.theme.NeonPurple
import com.dilara.beatify.ui.theme.NeonTextPrimary
import com.dilara.beatify.ui.theme.NeonTextSecondary

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
    onFavoriteClick: (() -> Unit)? = null
) {
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
            gradientType = GradientType.HORIZONTAL
        )
        TrackCardStyle.HORIZONTAL -> TrackCardConfig(
            cornerRadius = 20.dp,
            cardAlpha = 0.8f,
            padding = 16.dp,
            spacing = 16.dp,
            imageSize = 68.dp,
            imageCornerRadius = 16.dp,
            titleFontSize = 18.sp,
            artistFontSize = 14.sp,
            durationFontSize = 13.sp,
            showAlbumTitle = true,
            gradientType = GradientType.LINEAR
        )
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(config.cornerRadius)),
        shape = RoundedCornerShape(config.cornerRadius),
        colors = CardDefaults.cardColors(
            containerColor = DarkSurface.copy(alpha = config.cardAlpha)
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
                        GradientType.HORIZONTAL -> Brush.horizontalGradient(
                            colors = listOf(
                                NeonPurple.copy(alpha = 0.05f),
                                Transparent,
                                NeonCyan.copy(alpha = 0.05f)
                            )
                        )
                        GradientType.LINEAR -> Brush.linearGradient(
                            colors = listOf(
                                NeonPurple.copy(alpha = 0.1f),
                                Transparent,
                                NeonCyan.copy(alpha = 0.1f)
                            )
                        )
                    }
                )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { onClick() }
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
                            color = NeonTextPrimary,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                        
                        Spacer(modifier = Modifier.height(if (style == TrackCardStyle.VERTICAL) 4.dp else 6.dp))
                        
                        Text(
                            text = track.artist.name,
                            fontSize = config.artistFontSize,
                            fontWeight = FontWeight.Normal,
                            color = NeonTextSecondary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        
                        if (config.showAlbumTitle) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = track.album.title,
                                fontSize = 12.sp,
                                color = NeonTextSecondary.copy(alpha = 0.7f),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }

                    // Duration - favori butonu varsa gösterilmez
                    if (onFavoriteClick == null) {
                        Text(
                            text = formatDuration(track.duration),
                            fontSize = config.durationFontSize,
                            color = NeonTextSecondary,
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
