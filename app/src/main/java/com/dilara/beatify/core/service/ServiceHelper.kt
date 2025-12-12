package com.dilara.beatify.core.service

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import com.dilara.beatify.domain.model.Track

object ServiceHelper {
    private var musicService: MusicService? = null
    private var isBound = false
    private var pendingTrack: Track? = null
    private var pendingPlaylist: List<Track>? = null
    private var pendingIndex: Int? = null
    
    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as? MusicService.LocalBinder
            musicService = binder?.getService()
            isBound = true
            
            // Service bağlandıktan sonra pending track varsa foreground service'i başlat
            pendingTrack?.let { track ->
                musicService?.startForegroundService(track, pendingPlaylist, pendingIndex)
                pendingTrack = null
                pendingPlaylist = null
                pendingIndex = null
            }
        }
        
        override fun onServiceDisconnected(name: ComponentName?) {
            musicService = null
            isBound = false
        }
    }
    
    fun startService(
        context: Context,
        track: Track?,
        playlist: List<Track>? = null,
        currentIndex: Int? = null
    ) {
        // If service is already bound, update track immediately
        if (isBound && musicService != null) {
            musicService?.startForegroundService(track, playlist, currentIndex)
            return
        }
        
        pendingTrack = track
        pendingPlaylist = playlist
        pendingIndex = currentIndex
        val intent = Intent(context, MusicService::class.java)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            context.startForegroundService(intent)
        } else {
            context.startService(intent)
        }
        context.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
    }
    
    fun stopService(context: Context) {
        if (isBound) {
            context.unbindService(serviceConnection)
            isBound = false
        }
        val intent = Intent(context, MusicService::class.java)
        context.stopService(intent)
        musicService = null
    }
    
    fun updateNotification(
        track: Track?,
        isPlaying: Boolean,
        playlist: List<Track>? = null,
        currentIndex: Int? = null
    ) {
        musicService?.updateTrack(track, isPlaying, playlist, currentIndex)
    }
    
    fun getService(): MusicService? = musicService
}

