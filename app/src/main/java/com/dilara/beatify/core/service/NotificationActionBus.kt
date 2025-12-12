package com.dilara.beatify.core.service

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.SharedFlow

enum class NotificationAction {
    PLAY_PAUSE, NEXT, PREVIOUS, STOP
}

/**
 * Simple event bus to deliver notification actions to ViewModel without binding View layer.
 */
object NotificationActionBus {
    private val _actions = MutableSharedFlow<NotificationAction>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val actions: SharedFlow<NotificationAction> = _actions.asSharedFlow()

    suspend fun emit(action: NotificationAction) {
        _actions.emit(action)
    }
}


