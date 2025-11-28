package com.dilara.beatify.core.player

import android.content.Context
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.audio.AudioAttributes
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlayerStateHolder @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private var exoPlayer: ExoPlayer? = null

    fun getPlayer(): ExoPlayer {
        if (exoPlayer == null) {
            exoPlayer = ExoPlayer.Builder(context)
                .build()
                .apply {
                    repeatMode = Player.REPEAT_MODE_OFF
                    playWhenReady = false
                    setAudioAttributes(
                        AudioAttributes.Builder()
                            .setContentType(C.AUDIO_CONTENT_TYPE_MUSIC)
                            .setUsage(C.USAGE_MEDIA)
                            .build(),
                        true
                    )
                    volume = 1.0f
                }
        }
        return exoPlayer!!
    }

    fun releasePlayer() {
        exoPlayer?.release()
        exoPlayer = null
    }

    fun play(url: String) {
        val player = getPlayer()
        
        player.playWhenReady = false
        
        player.clearMediaItems()
        
        player.seekTo(0)
        
        val mediaItem = MediaItem.fromUri(url)
        player.addMediaItem(mediaItem)
        
        player.volume = 1.0f
        
        player.playWhenReady = true
        
        player.prepare()
        
        player.playWhenReady = true
    }

    fun pause() {
        val player = exoPlayer ?: return
        player.playWhenReady = false
    }

    fun resume() {
        val player = exoPlayer ?: return
        if (player.playbackState == Player.STATE_READY || player.playbackState == Player.STATE_BUFFERING) {
            player.playWhenReady = true
        }
    }

    fun seekTo(position: Long) {
        exoPlayer?.seekTo(position)
    }
}

