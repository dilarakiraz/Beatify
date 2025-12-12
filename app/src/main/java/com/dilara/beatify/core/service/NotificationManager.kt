package com.dilara.beatify.core.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.dilara.beatify.MainActivity
import com.dilara.beatify.R
import com.dilara.beatify.domain.model.Track
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BeatifyNotificationManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val notificationManager = NotificationManagerCompat.from(context)
    
    companion object {
        private const val CHANNEL_ID = "beatify_music_channel"
        private const val CHANNEL_NAME = "Müzik Çalma"
        const val NOTIFICATION_ID = 1
        
        // Action IDs
        const val ACTION_PLAY_PAUSE = "com.dilara.beatify.PLAY_PAUSE"
        const val ACTION_NEXT = "com.dilara.beatify.NEXT"
        const val ACTION_PREVIOUS = "com.dilara.beatify.PREVIOUS"
        const val ACTION_STOP = "com.dilara.beatify.STOP"
    }
    
    init {
        createNotificationChannel()
    }
    
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Müzik çalma bildirimleri"
                setShowBadge(false)
            }
            
            val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }
    
    fun createNotification(
        track: Track?,
        isPlaying: Boolean,
        coverBitmap: Bitmap? = null,
        mediaSessionToken: android.support.v4.media.session.MediaSessionCompat.Token? = null
    ): Notification {
        val mainIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
        val mainPendingIntent = PendingIntent.getActivity(
            context,
            0,
            mainIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        
        // Media action intents to MusicService
        fun servicePendingIntent(action: String, requestCode: Int): PendingIntent {
            val intent = Intent(context, MusicService::class.java).apply {
                this.action = action
            }
            return PendingIntent.getService(
                context,
                requestCode,
                intent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
        }

        val playPausePendingIntent = servicePendingIntent(ACTION_PLAY_PAUSE, 0)
        val nextPendingIntent = servicePendingIntent(ACTION_NEXT, 1)
        val previousPendingIntent = servicePendingIntent(ACTION_PREVIOUS, 2)
        val stopPendingIntent = servicePendingIntent(ACTION_STOP, 3)
        
        val title = track?.title ?: context.getString(R.string.app_name)
        val artist = track?.artist?.name ?: ""
        val text = if (track != null) {
            // Show artist name, and app name as subtitle
            "$artist • ${context.getString(R.string.app_name)}"
        } else {
            context.getString(R.string.app_name)
        }
        
        val largeIcon = coverBitmap ?: android.graphics.BitmapFactory.decodeResource(
            context.resources,
            R.mipmap.ic_launcher
        )
        
        return NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setContentText(text)
            .setSubText(context.getString(R.string.app_name)) // App name as subtitle
            .setLargeIcon(largeIcon)
            .setContentIntent(mainPendingIntent)
            .setStyle(
                androidx.media.app.NotificationCompat.MediaStyle()
                    .setShowActionsInCompactView(0, 1, 2)
                    .setMediaSession(mediaSessionToken)
            )
            .addAction(
                R.drawable.ic_previous,
                context.getString(R.string.cd_previous),
                previousPendingIntent
            )
            .addAction(
                if (isPlaying) R.drawable.ic_pause else R.drawable.ic_play,
                if (isPlaying) context.getString(R.string.cd_pause) else context.getString(R.string.cd_play),
                playPausePendingIntent
            )
            .addAction(
                R.drawable.ic_next,
                context.getString(R.string.cd_next),
                nextPendingIntent
            )
            .addAction(
                R.drawable.ic_close,
                context.getString(R.string.cd_delete),
                stopPendingIntent
            )
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setOnlyAlertOnce(true)
            .setOngoing(isPlaying)
            .setAutoCancel(false)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()
    }
    
    fun showNotification(notification: Notification) {
        // Android 13+ (API 33+) için POST_NOTIFICATIONS izni kontrolü
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    context,
                    android.Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // İzin yoksa bildirim gösterilemez
                return
            }
        }
        
        // NotificationManagerCompat.areNotificationsEnabled() kontrolü
        if (!notificationManager.areNotificationsEnabled()) {
            // Bildirimler kullanıcı tarafından devre dışı bırakılmış
            return
        }
        
        try {
            notificationManager.notify(NOTIFICATION_ID, notification)
        } catch (_: SecurityException) {
            // İzin reddedilmişse güvenli bir şekilde handle et
        }
    }
    
    fun hideNotification() {
        notificationManager.cancel(NOTIFICATION_ID)
    }
}

