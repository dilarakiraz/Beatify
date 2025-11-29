package com.dilara.beatify.presentation.ui.components.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dilara.beatify.ui.theme.DarkSurface
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
    BeatifyBottomSheet(
        onDismiss = onDismiss,
        modifier = modifier
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

