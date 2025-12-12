package com.dilara.beatify.presentation.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.dilara.beatify.ui.theme.DarkSurface
import com.dilara.beatify.ui.theme.LightPrimary
import com.dilara.beatify.ui.theme.LightSecondary
import com.dilara.beatify.ui.theme.LightSurface
import com.dilara.beatify.ui.theme.LightTertiary
import com.dilara.beatify.ui.theme.NeonCyan
import com.dilara.beatify.ui.theme.NeonPink
import com.dilara.beatify.ui.theme.NeonPurple
import com.dilara.beatify.ui.theme.isDarkTheme
import com.dilara.beatify.ui.theme.themeTextPrimary
import com.dilara.beatify.ui.theme.themeTextSecondary
import com.dilara.beatify.core.utils.stringResource
import com.dilara.beatify.R

@Composable
fun BeatifySearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onClearClick: () -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String? = null
) {
    val defaultPlaceholder = stringResource(R.string.search_placeholder)
    val displayPlaceholder = placeholder ?: defaultPlaceholder
    val alpha by animateFloatAsState(
        targetValue = if (query.isNotEmpty()) 1f else 0.6f,
        animationSpec = tween(300),
        label = "searchBarAlpha"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(
                brush = if (isDarkTheme) {
                    Brush.horizontalGradient(
                        colors = listOf(
                            NeonPurple.copy(alpha = 0.1f),
                            NeonCyan.copy(alpha = 0.05f),
                            NeonPink.copy(alpha = 0.1f)
                        )
                    )
                } else {
                    Brush.horizontalGradient(
                        colors = listOf(
                            LightPrimary.copy(alpha = 0.12f),
                            LightTertiary.copy(alpha = 0.08f),
                            LightSecondary.copy(alpha = 0.06f)
                        )
                    )
                }
            )
            .padding(2.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .clip(RoundedCornerShape(14.dp))
        ) {
            TextField(
                value = query,
                onValueChange = onQueryChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                placeholder = {
                    Text(
                        text = displayPlaceholder,
                        color = themeTextSecondary.copy(alpha = 0.7f)
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = stringResource(R.string.cd_search),
                        tint = if (isDarkTheme) NeonCyan else LightPrimary,
                        modifier = Modifier.size(22.dp)
                    )
                },
                trailingIcon = {
                    if (query.isNotEmpty()) {
                        IconButton(
                            onClick = onClearClick,
                            modifier = Modifier.size(40.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = stringResource(R.string.cd_clear),
                                tint = themeTextSecondary,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                },
                colors = TextFieldDefaults.colors(
                    // Container color'u transparent yap ki arka plandaki gradient görünsün
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    focusedTextColor = themeTextPrimary,
                    unfocusedTextColor = themeTextPrimary,
                    disabledTextColor = themeTextSecondary,
                    disabledContainerColor = Color.Transparent
                ),
                shape = RoundedCornerShape(14.dp),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                    }
                )
            )
        }
    }
}

