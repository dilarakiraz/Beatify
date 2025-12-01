package com.dilara.beatify.presentation.ui.components.player

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dilara.beatify.ui.theme.DarkSurface
import com.dilara.beatify.ui.theme.NeonCyan
import com.dilara.beatify.ui.theme.NeonTextSecondary

@Composable
fun PlayerSeekBar(
    position: Long,
    duration: Long,
    onSeekTo: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val progress = if (duration > 0) (position.toFloat() / duration.toFloat()).coerceIn(0f, 1f) else 0f

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = formatTime(position),
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = NeonCyan
            )
            Text(
                text = formatTime(duration),
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = NeonTextSecondary
            )
        }

        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            val trackWidth = maxWidth

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(Unit) {
                        detectTapGestures { tapOffset ->
                            if (duration > 0 && constraints.maxWidth > 0) {
                                val newProgress = (tapOffset.x / constraints.maxWidth).coerceIn(0f, 1f)
                                val newPosition = (newProgress * duration).toLong()
                                onSeekTo(newPosition)
                            }
                        }
                    }
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(5.dp)
                    .clip(RoundedCornerShape(2.5.dp))
                    .background(DarkSurface.copy(alpha = 0.4f))
            )

            Box(
                modifier = Modifier
                    .height(5.dp)
                    .width((trackWidth * progress).coerceAtMost(trackWidth))
                    .clip(RoundedCornerShape(2.5.dp))
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                NeonCyan.copy(alpha = 0.7f),
                                NeonCyan.copy(alpha = 0.85f),
                                NeonCyan.copy(alpha = 0.7f)
                            )
                        )
                    )
            )

            Box(
                modifier = Modifier
                    .offset(x = (trackWidth * progress - 10.dp).coerceAtLeast(0.dp).coerceAtMost(trackWidth - 20.dp))
                    .size(20.dp)
                    .clip(CircleShape)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                NeonCyan.copy(alpha = 0.8f),
                                NeonCyan.copy(alpha = 0.6f)
                            )
                        )
                    )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    Color.White.copy(alpha = 0.3f),
                                    Transparent
                                )
                            )
                        )
                )
            }
        }
    }
}

