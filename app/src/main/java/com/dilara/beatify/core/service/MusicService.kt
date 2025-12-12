package com.dilara.beatify.core.service

import android.app.Service
import android.content.Intent
import android.graphics.Bitmap
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import com.dilara.beatify.core.player.PlayerStateHolder
import com.dilara.beatify.domain.model.Track
import com.google.android.exoplayer2.Player
import dagger.hilt.android.AndroidEntryPoint
import androidx.core.graphics.drawable.toBitmap
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class MusicService : Service() {
    
    @Inject
    lateinit var playerStateHolder: PlayerStateHolder
    
    @Inject
    lateinit var notificationManager: BeatifyNotificationManager
    
    private val binder = LocalBinder()
    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    
    private var mediaSession: MediaSessionCompat? = null
    private var currentTrack: Track? = null
    private var isPlaying = false
    private var currentCover: Bitmap? = null
    private var playlist: List<Track> = emptyList()
    private var currentIndex: Int = -1
    
    private val playerListener = object : Player.Listener {
        override fun onPlaybackStateChanged(playbackState: Int) {
            updatePlaybackState()
        }
        
        override fun onIsPlayingChanged(isPlaying: Boolean) {
            // Update state immediately
            this@MusicService.isPlaying = isPlaying
            // Update notification and playback state
            updateNotification()
            updatePlaybackState()
        }
    }
    
    
    inner class LocalBinder : Binder() {
        fun getService(): MusicService = this@MusicService
    }
    
    override fun onCreate() {
        super.onCreate()
        initializeMediaSession()
        setupPlayer()
    }
    
    override fun onBind(intent: Intent?): IBinder {
        return binder
    }
    
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            BeatifyNotificationManager.ACTION_PLAY_PAUSE -> {
                // Toggle in service first
                togglePlayPause()
                // Notify ViewModel to sync its state (but don't toggle again)
                // ViewModel will update its UI state based on player state
                serviceScope.launch {
                    NotificationActionBus.emit(NotificationAction.PLAY_PAUSE)
                }
            }
            BeatifyNotificationManager.ACTION_NEXT -> {
                // If ViewModel is not alive, fallback to internal playlist
                playNextInternal()
                serviceScope.launch {
                    NotificationActionBus.emit(NotificationAction.NEXT)
                }
            }
            BeatifyNotificationManager.ACTION_PREVIOUS -> {
                playPreviousInternal()
                serviceScope.launch {
                    NotificationActionBus.emit(NotificationAction.PREVIOUS)
                }
            }
            BeatifyNotificationManager.ACTION_STOP -> {
                serviceScope.launch {
                    NotificationActionBus.emit(NotificationAction.STOP)
                }
                stopForeground(Service.STOP_FOREGROUND_REMOVE)
                stopSelf()
            }
        }
        return START_STICKY
    }
    
    override fun onDestroy() {
        super.onDestroy()
        mediaSession?.release()
        playerStateHolder.getPlayer().removeListener(playerListener)
        notificationManager.hideNotification()
        serviceScope.cancel()
    }
    
    private fun initializeMediaSession() {
        mediaSession = MediaSessionCompat(this, "BeatifyMusicService").apply {
            setCallback(object : MediaSessionCompat.Callback() {
                override fun onPlay() {
                    togglePlayPause()
                    serviceScope.launch { NotificationActionBus.emit(NotificationAction.PLAY_PAUSE) }
                }
                
                override fun onPause() {
                    togglePlayPause()
                    serviceScope.launch { NotificationActionBus.emit(NotificationAction.PLAY_PAUSE) }
                }
                
                override fun onSkipToNext() {
                    playNextInternal()
                    serviceScope.launch { NotificationActionBus.emit(NotificationAction.NEXT) }
                }
                
                override fun onSkipToPrevious() {
                    playPreviousInternal()
                    serviceScope.launch { NotificationActionBus.emit(NotificationAction.PREVIOUS) }
                }
                
                override fun onStop() {
                    serviceScope.launch {
                        NotificationActionBus.emit(NotificationAction.STOP)
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        stopForeground(Service.STOP_FOREGROUND_REMOVE)
                    } else {
                        @Suppress("DEPRECATION")
                        stopForeground(true)
                    }
                    stopSelf()
                }
            })
            isActive = true
        }
    }
    
    private fun setupPlayer() {
        val player = playerStateHolder.getPlayer()
        player.addListener(playerListener)
        isPlaying = player.isPlaying
    }
    
    fun startForegroundService(
        track: Track?,
        playlist: List<Track>? = null,
        index: Int? = null
    ) {
        val trackChanged = currentTrack?.id != track?.id
        currentTrack = track
        if (playlist != null) this.playlist = playlist
        if (index != null) this.currentIndex = index
        
        // Only clear cover if track actually changed
        if (trackChanged) {
            currentCover = null
        }
        
        // Show immediate notification (with existing cover if available)
        val notification = notificationManager.createNotification(
            track = currentTrack,
            isPlaying = isPlaying,
            coverBitmap = currentCover,
            mediaSessionToken = getMediaSessionToken()
        )
        startForeground(BeatifyNotificationManager.NOTIFICATION_ID, notification)
        updateMediaMetadata(currentTrack, currentCover)

        // Async load cover if not available or track changed
        if (currentTrack != null && (currentCover == null || trackChanged)) {
            serviceScope.launch {
                val loaded = loadCover(currentTrack)
                if (loaded != null) {
                    currentCover = loaded
                    updateNotification(loadCover = false)
                    updateMediaMetadata(currentTrack, currentCover)
                }
            }
        }
    }
    
    fun updateNotification(loadCover: Boolean = true) {
        val notification = notificationManager.createNotification(
            track = currentTrack,
            isPlaying = isPlaying,
            coverBitmap = currentCover,
            mediaSessionToken = getMediaSessionToken()
        )
        notificationManager.showNotification(notification)
        updateMediaMetadata(currentTrack, currentCover)

        // Always try to load cover if not available and track exists
        if (loadCover && currentCover == null && currentTrack != null) {
            serviceScope.launch {
                val track = currentTrack // Capture current track
                if (track != null) { // Double check it's still valid
                    val loaded = loadCover(track)
                    // Verify track hasn't changed while loading
                    if (loaded != null && currentTrack?.id == track.id) {
                        currentCover = loaded
                        updateNotification(loadCover = false)
                        updateMediaMetadata(currentTrack, currentCover)
                    }
                }
            }
        }
    }
    
    fun updateTrack(
        track: Track?,
        isPlaying: Boolean? = null,
        playlist: List<Track>? = null,
        currentIndex: Int? = null
    ) {
        val trackChanged = currentTrack?.id != track?.id
        currentTrack = track
        if (isPlaying != null) {
            this.isPlaying = isPlaying
        }
        if (playlist != null) this.playlist = playlist
        if (currentIndex != null) this.currentIndex = currentIndex
        
        // Only clear cover if track actually changed
        if (trackChanged) {
            currentCover = null
            // Load new cover immediately
            if (track != null) {
                serviceScope.launch {
                    val trackToLoad = track // Capture track
                    val loaded = loadCover(trackToLoad)
                    // Verify track hasn't changed while loading
                    if (loaded != null && currentTrack?.id == trackToLoad.id) {
                        currentCover = loaded
                        updateNotification(loadCover = false)
                        updateMediaMetadata(currentTrack, currentCover)
                    }
                }
            }
        }
        
        updateNotification(loadCover = !trackChanged) // Load cover if track didn't change
    }
    
    private fun updatePlaybackState() {
        val player = playerStateHolder.getPlayer()
        val state = when (player.playbackState) {
            Player.STATE_IDLE -> PlaybackStateCompat.STATE_NONE
            Player.STATE_BUFFERING -> PlaybackStateCompat.STATE_BUFFERING
            Player.STATE_READY -> if (isPlaying) {
                PlaybackStateCompat.STATE_PLAYING
            } else {
                PlaybackStateCompat.STATE_PAUSED
            }
            Player.STATE_ENDED -> PlaybackStateCompat.STATE_STOPPED
            else -> PlaybackStateCompat.STATE_NONE
        }
        
        val playbackState = PlaybackStateCompat.Builder()
            .setState(
                state,
                player.currentPosition,
                1.0f
            )
            .setActions(
                PlaybackStateCompat.ACTION_PLAY or
                PlaybackStateCompat.ACTION_PAUSE or
                PlaybackStateCompat.ACTION_SKIP_TO_NEXT or
                PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS or
                PlaybackStateCompat.ACTION_STOP
            )
            .build()
        
        mediaSession?.setPlaybackState(playbackState)
    }
    
    private fun togglePlayPause() {
        val player = playerStateHolder.getPlayer()
        
        // Check if player is ready to play
        if (player.playbackState != Player.STATE_READY && player.playbackState != Player.STATE_BUFFERING) {
            // Player not ready, try to prepare if we have a track
            val track = currentTrack
            val previewUrl = track?.previewUrl
            if (previewUrl != null) {
                try {
                    playerStateHolder.play(previewUrl)
                    // State will be updated by playerListener.onIsPlayingChanged
                    return
                } catch (e: Exception) {
                    // Ignore
                }
            }
            return
        }
        
        // Toggle playWhenReady based on current playing state
        // Use isPlaying state for more reliable toggle
        val currentlyPlaying = player.isPlaying
        player.playWhenReady = !currentlyPlaying
        
        // Update state immediately for UI feedback
        // This will be confirmed by listener, but gives immediate feedback
        isPlaying = !currentlyPlaying
        updateNotification()
        updatePlaybackState()
        
        // Listener will confirm the actual state
    }

    private fun playNextInternal() {
        if (playlist.isEmpty()) return
        val nextIndex = if (currentIndex + 1 < playlist.size) currentIndex + 1 else currentIndex
        playAtIndexInternal(nextIndex)
    }

    private fun playPreviousInternal() {
        if (playlist.isEmpty()) return
        val prevIndex = if (currentIndex - 1 >= 0) currentIndex - 1 else currentIndex
        playAtIndexInternal(prevIndex)
    }

    private fun playAtIndexInternal(index: Int) {
        if (playlist.isEmpty() || index !in playlist.indices) return
        val track = playlist[index]
        val url = track.previewUrl ?: return
        currentIndex = index
        currentTrack = track
        currentCover = null

        // Play on main thread
        launchOnMain {
            try {
                playerStateHolder.play(url)
                isPlaying = true
                updateNotification()
                updateMediaMetadata(currentTrack, currentCover)
                // load cover async
                serviceScope.launch {
                    val loaded = loadCover(track)
                    if (loaded != null) {
                        currentCover = loaded
                        updateNotification(loadCover = false)
                        updateMediaMetadata(currentTrack, currentCover)
                    }
                }
            } catch (_: Exception) {
                // ignore
            }
        }
    }

    private fun launchOnMain(block: suspend () -> Unit) {
        // use main dispatcher via serviceScope with context switch
        serviceScope.launch {
            withContext(Dispatchers.Main) {
                block()
            }
        }
    }

    private fun updateMediaMetadata(track: Track?, cover: Bitmap?) {
        val session = mediaSession ?: return
        val artist = track?.artist?.name ?: ""
        val title = track?.title ?: ""

        val metadataBuilder = MediaMetadataCompat.Builder()
            .putString(MediaMetadataCompat.METADATA_KEY_TITLE, title)
            .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, artist)
            .putString(MediaMetadataCompat.METADATA_KEY_ALBUM, track?.album?.title ?: "")

        if (cover != null) {
            metadataBuilder.putBitmap(MediaMetadataCompat.METADATA_KEY_ALBUM_ART, cover)
        }

        session.setMetadata(metadataBuilder.build())
    }
    
    private fun pause() {
        playerStateHolder.pause()
        // State will be updated by playerListener.onIsPlayingChanged
    }
    
    private fun resume() {
        playerStateHolder.resume()
        // State will be updated by playerListener.onIsPlayingChanged
    }
    
    fun getMediaSessionToken(): MediaSessionCompat.Token? {
        return mediaSession?.sessionToken
    }

    private suspend fun loadCover(track: Track?): Bitmap? {
        val url = track?.album?.coverXl
            ?: track?.album?.coverBig
            ?: track?.album?.coverMedium
            ?: track?.album?.cover
            ?: return null
        return withContext(Dispatchers.IO) {
            try {
                val loader = ImageLoader(this@MusicService)
                val request = ImageRequest.Builder(this@MusicService)
                    .data(url)
                    .allowHardware(false)
                    .build()
                val result = loader.execute(request)
                if (result is SuccessResult) {
                    result.drawable.toBitmap()
                } else {
                    null
                }
            } catch (_: Exception) {
                null
            }
        }
    }
}

