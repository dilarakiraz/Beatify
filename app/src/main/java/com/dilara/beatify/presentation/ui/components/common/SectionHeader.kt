package com.dilara.beatify.presentation.ui.components.common

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dilara.beatify.ui.theme.NeonCyan
import com.dilara.beatify.ui.theme.NeonTextSecondary

@Composable
fun SectionHeader(
    title: String,
    subtitle: String? = null,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = title,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = NeonCyan
        )
        subtitle?.let {
            Text(
                text = it,
                fontSize = 13.sp,
                color = NeonTextSecondary,
                modifier = Modifier.padding(top = 2.dp)
            )
        }
    }
}


