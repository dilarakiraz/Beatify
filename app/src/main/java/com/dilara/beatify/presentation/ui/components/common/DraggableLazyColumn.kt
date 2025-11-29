package com.dilara.beatify.presentation.ui.components.common

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun <T> DraggableLazyColumn(
    items: List<T>,
    onMove: (Int, Int) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    listState: LazyListState = rememberLazyListState(),
    key: ((T) -> Any)? = null,
    headerContent: (@Composable () -> Unit)? = null,
    itemContent: @Composable (Int, T, Boolean) -> Unit
) {
    var draggedIndex by remember { mutableStateOf<Int?>(null) }
    var targetIndex by remember { mutableStateOf<Int?>(null) }

    LazyColumn(
        state = listState,
        modifier = modifier,
        contentPadding = contentPadding,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (headerContent != null) {
            item {
                headerContent()
            }
        }
        
        itemsIndexed(
            items = items,
            key = if (key != null) { index, item -> key(item) } else null
        ) { index, item ->
            val isDragging = draggedIndex == index
            val elevation by animateDpAsState(
                targetValue = if (isDragging) 16.dp else 0.dp,
                label = "elevation"
            )
            
            val targetOffset by animateDpAsState(
                targetValue = if (targetIndex != null && targetIndex == index && draggedIndex != null && draggedIndex != index) {
                    val draggedIdx = draggedIndex ?: 0
                    if (draggedIdx < index) -72.dp else 72.dp
                } else 0.dp,
                label = "target_offset"
            )

            Box(
                modifier = Modifier
                    .zIndex(if (isDragging) 1f else 0f)
                    .pointerInput(index, items.size) {
                        detectDragGesturesAfterLongPress(
                            onDragStart = {
                                draggedIndex = index
                            },
                            onDrag = { change, dragAmount ->
                                change.consume()
                                
                                // Y eksenindeki hareket miktarına göre target index'i belirle
                                val currentTarget = targetIndex ?: index
                                val newTargetIndex = when {
                                    dragAmount.y > 50f -> minOf(items.size - 1, currentTarget + 1)
                                    dragAmount.y < -50f -> maxOf(0, currentTarget - 1)
                                    else -> currentTarget
                                }
                                
                                if (newTargetIndex != index && newTargetIndex in items.indices && newTargetIndex != currentTarget) {
                                    targetIndex = newTargetIndex
                                }
                            },
                            onDragEnd = {
                                val from = draggedIndex
                                val to = targetIndex
                                if (from != null && to != null && from != to && from in items.indices && to in items.indices) {
                                    onMove(from, to)
                                }
                                draggedIndex = null
                                targetIndex = null
                            },
                            onDragCancel = {
                                draggedIndex = null
                                targetIndex = null
                            }
                        )
                    }
                    .graphicsLayer {
                        shadowElevation = elevation.toPx()
                        alpha = if (isDragging) 0.9f else 1f
                        translationY = targetOffset.toPx()
                        scaleX = if (isDragging) 1.05f else 1f
                        scaleY = if (isDragging) 1.05f else 1f
                    }
            ) {
                itemContent(index, item, isDragging)
            }
        }
    }
}
