package com.dilara.beatify.presentation.ui.components.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun <T> HorizontalItemsList(
    items: List<T>,
    key: (T) -> Any,
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.spacedBy(16.dp),
    contentPadding: PaddingValues = PaddingValues(horizontal = 4.dp),
    content: @Composable (T) -> Unit
) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = horizontalArrangement,
        contentPadding = contentPadding
    ) {
        items(
            items = items,
            key = key
        ) { item ->
            content(item)
        }
    }
}



