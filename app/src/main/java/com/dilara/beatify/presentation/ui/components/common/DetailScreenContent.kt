package com.dilara.beatify.presentation.ui.components.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dilara.beatify.ui.theme.themeBackground

@Composable
fun DetailScreenContent(
    isLoading: Boolean,
    error: String?,
    isEmpty: Boolean,
    emptyMessage: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(
        start = 20.dp,
        end = 20.dp,
        top = 24.dp,
        bottom = 160.dp
    ),
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 320.dp)
    ) {
        when {
            isLoading -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(themeBackground),
                    contentPadding = contentPadding,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(5) {
                        TrackCardSkeleton()
                    }
                }
            }

            error != null -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(themeBackground),
                    contentPadding = contentPadding
                ) {
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
                    modifier = Modifier
                        .fillMaxSize()
                        .background(themeBackground),
                    contentPadding = contentPadding
                ) {
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
}



