package com.dilara.beatify.core.service

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import android.os.IBinder
import com.dilara.beatify.domain.model.Album
import com.dilara.beatify.domain.model.Artist
import com.dilara.beatify.domain.model.Track
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.anyOrNull
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.lang.reflect.Field

class ServiceHelperTest {

    private lateinit var mockContext: Context
    private lateinit var mockMusicService: MusicService
    private lateinit var mockBinder: MusicService.LocalBinder

    @Before
    fun setup() {
        mockContext = mock()
        mockMusicService = mock()
        mockBinder = mock()
        
        // ServiceHelper'ın state'ini reset etmek için reflection kullan
        resetServiceHelperState()
    }

    private fun resetServiceHelperState() {
        try {
            val musicServiceField: Field = ServiceHelper.javaClass.getDeclaredField("musicService")
            musicServiceField.isAccessible = true
            musicServiceField.set(null, null)

            val isBoundField: Field = ServiceHelper.javaClass.getDeclaredField("isBound")
            isBoundField.isAccessible = true
            isBoundField.set(null, false)

            val pendingTrackField: Field = ServiceHelper.javaClass.getDeclaredField("pendingTrack")
            pendingTrackField.isAccessible = true
            pendingTrackField.set(null, null)
        } catch (e: Exception) {
            // Reflection hatası - devam et
        }
    }

    private fun createTestTrack(): Track {
        val artist = Artist(
            id = 1L,
            name = "Test Artist",
            picture = null,
            pictureSmall = null,
            pictureMedium = null,
            pictureBig = null,
            pictureXl = null
        )
        val album = Album(
            id = 1L,
            title = "Test Album",
            cover = null,
            coverSmall = null,
            coverMedium = null,
            coverBig = null,
            coverXl = null,
            artist = artist
        )
        return Track(
            id = 1L,
            title = "Test Track",
            titleShort = "Test",
            duration = 180,
            previewUrl = "https://example.com/preview.mp3",
            artist = artist,
            album = album
        )
    }

    @Test
    fun `startService should start foreground service on Android O and above`() {
        // 1. HAZIRLIK
        val track = createTestTrack()
        whenever(mockContext.packageName).thenReturn("com.dilara.beatify")
        
        // Android O (API 26) ve üzeri için startForegroundService çağrılmalı
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // 2. ÇALIŞTIRMA
            ServiceHelper.startService(mockContext, track)

            // 3. KONTROL
            verify(mockContext).startForegroundService(any<Intent>())
            verify(mockContext, never()).startService(any<Intent>())
        } else {
            // Android O altı için startService çağrılmalı
            ServiceHelper.startService(mockContext, track)

            verify(mockContext).startService(any<Intent>())
            verify(mockContext, never()).startForegroundService(any<Intent>())
        }
        
        // bindService her zaman çağrılmalı
        verify(mockContext).bindService(
            any<Intent>(),
            any<ServiceConnection>(),
            eq(Context.BIND_AUTO_CREATE)
        )
    }

    @Test
    fun `startService should bind service`() {
        // 1. HAZIRLIK
        val track = createTestTrack()
        whenever(mockContext.packageName).thenReturn("com.dilara.beatify")

        // 2. ÇALIŞTIRMA
        ServiceHelper.startService(mockContext, track)

        // 3. KONTROL - bindService çağrılmalı (Intent içeriği Android SDK sınıfı olduğu için mock'lanamıyor)
        verify(mockContext).bindService(
            any<Intent>(),
            any<ServiceConnection>(),
            eq(Context.BIND_AUTO_CREATE)
        )
    }

    @Test
    fun `startService should store pending track when service not yet connected`() {
        // 1. HAZIRLIK
        val track = createTestTrack()
        whenever(mockContext.packageName).thenReturn("com.dilara.beatify")
        whenever(mockBinder.getService()).thenReturn(mockMusicService)

        // 2. ÇALIŞTIRMA
        ServiceHelper.startService(mockContext, track)

        // 3. ServiceConnection callback'ini manuel olarak tetikle
        val serviceConnection = getServiceConnection()
        serviceConnection?.onServiceConnected(
            ComponentName("com.dilara.beatify", MusicService::class.java.name),
            mockBinder as IBinder
        )

        // 4. KONTROL - Service bağlandıktan sonra startForegroundService çağrılmalı
        verify(mockMusicService).startForegroundService(eq(track))
    }

    @Test
    fun `startService with null track should not call startForegroundService`() {
        // 1. HAZIRLIK
        whenever(mockContext.packageName).thenReturn("com.dilara.beatify")
        whenever(mockBinder.getService()).thenReturn(mockMusicService)

        // 2. ÇALIŞTIRMA
        ServiceHelper.startService(mockContext, null)

        // 3. ServiceConnection callback'ini manuel olarak tetikle
        val serviceConnection = getServiceConnection()
        serviceConnection?.onServiceConnected(
            ComponentName("com.dilara.beatify", MusicService::class.java.name),
            mockBinder as IBinder
        )

        // 4. KONTROL - Track null olduğu için startForegroundService çağrılmamalı
        verify(mockMusicService, never()).startForegroundService(anyOrNull<Track>())
    }

    @Test
    fun `stopService should unbind service when bound`() {
        // 1. HAZIRLIK
        whenever(mockContext.packageName).thenReturn("com.dilara.beatify")
        whenever(mockBinder.getService()).thenReturn(mockMusicService)

        // Service'i başlat ve bağla
        ServiceHelper.startService(mockContext, null)
        val serviceConnection = getServiceConnection()
        serviceConnection?.onServiceConnected(
            ComponentName("com.dilara.beatify", MusicService::class.java.name),
            mockBinder as IBinder
        )

        // 2. ÇALIŞTIRMA
        ServiceHelper.stopService(mockContext)

        // 3. KONTROL
        verify(mockContext).unbindService(any<ServiceConnection>())
        verify(mockContext).stopService(any<Intent>())
    }

    @Test
    fun `stopService should stop service even when not bound`() {
        // 1. HAZIRLIK
        whenever(mockContext.packageName).thenReturn("com.dilara.beatify")

        // 2. ÇALIŞTIRMA (Service bağlı değil)
        ServiceHelper.stopService(mockContext)

        // 3. KONTROL
        verify(mockContext, never()).unbindService(any<ServiceConnection>())
        verify(mockContext).stopService(any<Intent>())
    }

    @Test
    fun `updateNotification should call service methods when service is bound`() {
        // 1. HAZIRLIK
        val track = createTestTrack()
        whenever(mockContext.packageName).thenReturn("com.dilara.beatify")
        whenever(mockBinder.getService()).thenReturn(mockMusicService)

        // Service'i başlat ve bağla
        ServiceHelper.startService(mockContext, null)
        val serviceConnection = getServiceConnection()
        serviceConnection?.onServiceConnected(
            ComponentName("com.dilara.beatify", MusicService::class.java.name),
            mockBinder as IBinder
        )

        // 2. ÇALIŞTIRMA
        ServiceHelper.updateNotification(track, isPlaying = true)

        // 3. KONTROL
        verify(mockMusicService).updateTrack(eq(track))
        verify(mockMusicService).updateNotification()
    }

    @Test
    fun `updateNotification should not crash when service is not bound`() {
        // 1. HAZIRLIK
        val track = createTestTrack()

        // 2. ÇALIŞTIRMA (Service bağlı değil - crash olmamalı)
        ServiceHelper.updateNotification(track, isPlaying = false)

        // 3. KONTROL - Service bağlı olmadığı için hiçbir şey çağrılmamalı
        verify(mockMusicService, never()).updateTrack(anyOrNull<Track>())
        verify(mockMusicService, never()).updateNotification()
    }

    @Test
    fun `getService should return null when service is not bound`() {
        // 1. HAZIRLIK - Service başlatılmamış

        // 2. ÇALIŞTIRMA
        val service = ServiceHelper.getService()

        // 3. KONTROL
        assertThat(service).isNull()
    }

    @Test
    fun `getService should return service when service is bound`() {
        // 1. HAZIRLIK
        whenever(mockContext.packageName).thenReturn("com.dilara.beatify")
        whenever(mockBinder.getService()).thenReturn(mockMusicService)

        // Service'i başlat ve bağla
        ServiceHelper.startService(mockContext, null)
        val serviceConnection = getServiceConnection()
        serviceConnection?.onServiceConnected(
            ComponentName("com.dilara.beatify", MusicService::class.java.name),
            mockBinder as IBinder
        )

        // 2. ÇALIŞTIRMA
        val service = ServiceHelper.getService()

        // 3. KONTROL
        assertThat(service).isNotNull()
        assertThat(service).isEqualTo(mockMusicService)
    }

    @Test
    fun `onServiceDisconnected should clear service reference`() {
        // 1. HAZIRLIK
        whenever(mockContext.packageName).thenReturn("com.dilara.beatify")
        whenever(mockBinder.getService()).thenReturn(mockMusicService)

        // Service'i başlat ve bağla
        ServiceHelper.startService(mockContext, null)
        val serviceConnection = getServiceConnection()
        serviceConnection?.onServiceConnected(
            ComponentName("com.dilara.beatify", MusicService::class.java.name),
            mockBinder as IBinder
        )

        // Service bağlı olduğunu kontrol et
        assertThat(ServiceHelper.getService()).isNotNull()

        // 2. ÇALIŞTIRMA - Service bağlantısı kesildi
        serviceConnection?.onServiceDisconnected(
            ComponentName("com.dilara.beatify", MusicService::class.java.name)
        )

        // 3. KONTROL
        assertThat(ServiceHelper.getService()).isNull()
    }

    // Helper method to get ServiceConnection using reflection
    private fun getServiceConnection(): ServiceConnection? {
        return try {
            val field: Field = ServiceHelper.javaClass.getDeclaredField("serviceConnection")
            field.isAccessible = true
            field.get(null) as? ServiceConnection
        } catch (e: Exception) {
            null
        }
    }
}

