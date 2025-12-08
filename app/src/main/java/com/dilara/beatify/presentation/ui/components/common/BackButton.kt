package com.dilara.beatify.presentation.ui.components.common

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.dilara.beatify.ui.theme.NeonTextPrimary
import com.dilara.beatify.core.utils.stringResource
import com.dilara.beatify.R

@Composable
fun BackButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    GlassIconButton(
        icon = Icons.Default.ArrowBack,
        onClick = onClick,
        modifier = modifier,
        style = GlassIconButtonStyle.PRIMARY,
        iconTint = NeonTextPrimary,
        contentDescription = stringResource(R.string.cd_back)
    )
}

