package com.dilara.beatify.presentation.ui.components.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.dilara.beatify.ui.theme.DarkBackground
import com.dilara.beatify.ui.theme.DarkSurface
import com.dilara.beatify.ui.theme.LightBackground
import com.dilara.beatify.ui.theme.LightPrimary
import com.dilara.beatify.ui.theme.LightSurface
import com.dilara.beatify.ui.theme.LightTertiary
import com.dilara.beatify.ui.theme.NeonCyan
import com.dilara.beatify.ui.theme.isDarkTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BeatifyBottomSheet(
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)
    
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        modifier = modifier.fillMaxHeight(),
        containerColor = Color.Transparent,
        dragHandle = {
            Box(
                modifier = Modifier
                    .width(40.dp)
                    .height(4.dp)
                    .padding(vertical = 12.dp)
                    .background(
                        if (isDarkTheme) NeonCyan.copy(alpha = 0.4f)
                        else LightPrimary.copy(alpha = 0.5f),
                        RoundedCornerShape(2.dp)
                    )
            )
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = if (isDarkTheme) {
                        Brush.verticalGradient(
                            colors = listOf(
                                DarkBackground.copy(alpha = 0.98f),
                                DarkSurface.copy(alpha = 0.96f)
                            )
                        )
                    } else {
                        Brush.verticalGradient(
                            colors = listOf(
                                LightBackground.copy(alpha = 1f),
                                LightSurface.copy(alpha = 0.98f)
                            )
                        )
                    }
                )
        ) {
            // Subtle gradient overlay (mini player gibi)
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = if (isDarkTheme) {
                            Brush.radialGradient(
                                colors = listOf(
                                    com.dilara.beatify.ui.theme.NeonPurple.copy(alpha = 0.08f),
                                    com.dilara.beatify.ui.theme.NeonCyan.copy(alpha = 0.05f),
                                    Color.Transparent
                                ),
                                radius = 800f
                            )
                        } else {
                            Brush.radialGradient(
                                colors = listOf(
                                    LightPrimary.copy(alpha = 0.12f),
                                    LightTertiary.copy(alpha = 0.09f),
                                    com.dilara.beatify.ui.theme.LightSecondary.copy(alpha = 0.06f),
                                    Color.Transparent
                                ),
                                radius = 800f
                            )
                        }
                    )
            )
            
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                content()
            }
        }
    }
}
