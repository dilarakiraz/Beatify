package com.dilara.beatify.core.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

/**
 * BroadcastReceiver for handling notification actions.
 * - Manifest-registered usage: relies on default no-op callbacks (avoids crash).
 * - In-app usage: pass callbacks to forward actions to ViewModel.
 */
class MusicNotificationReceiver(
    private val onPlayPause: () -> Unit = {},
    private val onNext: () -> Unit = {},
    private val onPrevious: () -> Unit = {},
    private val onStop: () -> Unit = {}
) : BroadcastReceiver() {
    
    override fun onReceive(context: Context?, intent: Intent?) {
        when (intent?.action) {
            BeatifyNotificationManager.ACTION_PLAY_PAUSE -> {
                onPlayPause()
            }
            BeatifyNotificationManager.ACTION_NEXT -> {
                onNext()
            }
            BeatifyNotificationManager.ACTION_PREVIOUS -> {
                onPrevious()
            }
            BeatifyNotificationManager.ACTION_STOP -> {
                onStop()
            }
        }
    }
}

