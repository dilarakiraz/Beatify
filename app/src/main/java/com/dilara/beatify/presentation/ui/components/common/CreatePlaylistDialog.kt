package com.dilara.beatify.presentation.ui.components.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dilara.beatify.ui.theme.DarkBackground
import com.dilara.beatify.ui.theme.DarkSurface
import com.dilara.beatify.ui.theme.NeonCyan
import com.dilara.beatify.ui.theme.NeonPurple
import com.dilara.beatify.ui.theme.NeonTextPrimary
import com.dilara.beatify.ui.theme.NeonTextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePlaylistDialog(
    playlistName: String,
    onPlaylistNameChange: (String) -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        modifier = modifier,
        containerColor = Color.Transparent,
        dragHandle = {
            Box(
                modifier = Modifier
                    .width(40.dp)
                    .height(4.dp)
                    .padding(vertical = 12.dp)
                    .background(
                        NeonCyan.copy(alpha = 0.4f),
                        RoundedCornerShape(2.dp)
                    )
            )
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            DarkBackground.copy(alpha = 0.98f),
                            DarkSurface.copy(alpha = 0.96f)
                        )
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Text(
                    text = "Yeni Çalma Listesi",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = NeonTextPrimary
                )
                
                TextField(
                    value = playlistName,
                    onValueChange = onPlaylistNameChange,
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = {
                        Text(
                            text = "Çalma listesi adı",
                            color = NeonTextSecondary.copy(alpha = 0.7f)
                        )
                    },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = DarkSurface.copy(alpha = 0.8f),
                        unfocusedContainerColor = DarkSurface.copy(alpha = 0.6f),
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedTextColor = NeonTextPrimary,
                        unfocusedTextColor = NeonTextPrimary
                    ),
                    shape = RoundedCornerShape(16.dp),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            if (playlistName.isNotBlank()) {
                                onConfirm()
                            }
                        }
                    )
                )
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    GradientButton(
                        text = "İptal",
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        isSecondary = true
                    )
                    
                    GradientButton(
                        text = "Oluştur",
                        onClick = {
                            if (playlistName.isNotBlank()) {
                                onConfirm()
                            }
                        },
                        modifier = Modifier.weight(1f),
                        enabled = playlistName.isNotBlank()
                    )
                }
            }
        }
    }
}

@Composable
private fun GradientButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isSecondary: Boolean = false,
    enabled: Boolean = true
) {
    Box(
        modifier = modifier
            .height(52.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(
                brush = if (isSecondary) {
                    Brush.horizontalGradient(
                        colors = listOf(
                            DarkSurface.copy(alpha = 0.6f),
                            DarkSurface.copy(alpha = 0.4f)
                        )
                    )
                } else if (enabled) {
                    Brush.horizontalGradient(
                        colors = listOf(
                            NeonCyan.copy(alpha = 0.35f),
                            NeonPurple.copy(alpha = 0.3f)
                        )
                    )
                } else {
                    Brush.horizontalGradient(
                        colors = listOf(
                            NeonTextSecondary.copy(alpha = 0.3f),
                            NeonTextSecondary.copy(alpha = 0.2f)
                        )
                    )
                }
            )
            .then(
                if (enabled || isSecondary) {
                    Modifier.clickable(onClick = onClick)
                } else {
                    Modifier
                }
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = if (isSecondary) {
                NeonTextSecondary
            } else if (enabled) {
                NeonTextPrimary
            } else {
                NeonTextSecondary.copy(alpha = 0.5f)
            }
        )
    }
}

