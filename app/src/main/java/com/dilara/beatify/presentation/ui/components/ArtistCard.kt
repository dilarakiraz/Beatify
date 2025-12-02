package com.dilara.beatify.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.dilara.beatify.domain.model.Artist
import com.dilara.beatify.ui.theme.DarkSurface
import com.dilara.beatify.ui.theme.LightSurface
import com.dilara.beatify.ui.theme.isDarkTheme

@Composable
fun ArtistCard(
    artist: Artist,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    size: androidx.compose.ui.unit.Dp = 120.dp
) {
    val cardColor = if (isDarkTheme) {
        DarkSurface.copy(alpha = 0.6f)
    } else {
        LightSurface.copy(alpha = 0.95f)
    }
    
    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(cardColor)
            .clickable(onClick = onClick)
    ) {
        if (artist.pictureBig != null || artist.pictureMedium != null) {
            AsyncImage(
                model = artist.pictureBig ?: artist.pictureMedium,
                contentDescription = artist.name,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.5f)
                        )
                    )
                )
        )

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = artist.name,
                fontSize = 12.sp,
                fontWeight = androidx.compose.ui.text.font.FontWeight.Medium,
                color = Color.White,  // Her iki temada da beyaz (overlay üzerinde)
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center
            )
        }
    }
}

