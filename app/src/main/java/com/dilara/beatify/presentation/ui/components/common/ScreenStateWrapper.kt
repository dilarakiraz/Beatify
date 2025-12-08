package com.dilara.beatify.presentation.ui.components.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dilara.beatify.ui.theme.themeBackground

@Composable
fun ScreenStateWrapper(
    isLoading: Boolean,
    error: String?,
    isEmpty: Boolean,
    emptyMessage: String,
    onRetry: () -> Unit,
    headerContent: @Composable () -> Unit,
    contentPadding: PaddingValues = PaddingValues(
        start = 16.dp,
        end = 16.dp,
        top = 16.dp,
        bottom = 8.dp
    ),
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    when {
        isLoading -> {
            LazyColumn(
                modifier = modifier
                    .fillMaxSize()
                    .background(themeBackground),
                contentPadding = contentPadding,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item { headerContent() }
                items(5) {
                    LoadingSkeleton()
                }
            }
        }

        error != null -> {
            LazyColumn(
                modifier = modifier
                    .fillMaxSize()
                    .background(themeBackground),
                contentPadding = contentPadding,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item { headerContent() }
                item {
                    ErrorSection(
                        message = error,
                        onRetry = onRetry
                    )
                }
            }
        }

        isEmpty -> {
            LazyColumn(
                modifier = modifier
                    .fillMaxSize()
                    .background(themeBackground),
                contentPadding = contentPadding,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item { headerContent() }
                item {
                    EmptySection(message = emptyMessage)
                }
            }
        }

        else -> {
            content()
        }
    }
}

