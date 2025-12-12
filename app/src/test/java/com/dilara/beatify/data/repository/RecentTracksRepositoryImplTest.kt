package com.dilara.beatify.data.repository

import app.cash.turbine.test
import com.dilara.beatify.data.local.dao.RecentTrackDao
import com.dilara.beatify.data.local.entity.RecentTrackEntity
import com.dilara.beatify.domain.model.Album
import com.dilara.beatify.domain.model.Artist
import com.dilara.beatify.domain.model.Track
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.*
import com.google.common.truth.Truth.assertThat

class RecentTracksRepositoryImplTest {
    
    // Mock: Sahte veritabanı
    private lateinit var recentTrackDao: RecentTrackDao
    private lateinit var repository: RecentTracksRepositoryImpl
    
    @Before
    fun setup() {
        // Her test öncesi yeni mock oluştur
        recentTrackDao = mock()
        repository = RecentTracksRepositoryImpl(recentTrackDao)
    }
    
    // TEST 1: Son dinlenen şarkıları getirme
    @Test
    fun `getRecentTracks should return list of tracks`() = runTest {
        // 1. HAZIRLIK
        val entity = createRecentTrackEntity()
        whenever(recentTrackDao.getRecentTracks(50)).thenReturn(flowOf(listOf(entity)))
        
        // 2. ÇALIŞTIRMA
        val result = repository.getRecentTracks(50).first()
        
        // 3. KONTROL
        assertThat(result).isNotEmpty()
        assertThat(result.size).isEqualTo(1)
        assertThat(result[0].id).isEqualTo(1L)
        assertThat(result[0].title).isEqualTo("Test Track")
    }
    
    // TEST 2: Boş son dinlenenler listesi
    @Test
    fun `getRecentTracks should return empty list when no recent tracks`() = runTest {
        // 1. HAZIRLIK
        whenever(recentTrackDao.getRecentTracks(50)).thenReturn(flowOf(emptyList()))
        
        // 2. ÇALIŞTIRMA
        val result = repository.getRecentTracks(50).first()
        
        // 3. KONTROL
        assertThat(result).isEmpty()
    }
    
    // TEST 3: Limit parametresi ile getirme
    @Test
    fun `getRecentTracks should respect limit parameter`() = runTest {
        // 1. HAZIRLIK
        val entities = listOf(
            createRecentTrackEntity(id = 1L),
            createRecentTrackEntity(id = 2L)
        )
        whenever(recentTrackDao.getRecentTracks(10)).thenReturn(flowOf(entities))
        
        // 2. ÇALIŞTIRMA
        val result = repository.getRecentTracks(10).first()
        
        // 3. KONTROL
        assertThat(result).hasSize(2)
        verify(recentTrackDao).getRecentTracks(10)
    }
    
    // TEST 4: Son dinlenen şarkı ekleme
    @Test
    fun `addRecentTrack should insert track successfully`() = runTest {
        // 1. HAZIRLIK
        val track = createTestTrack()
        whenever(recentTrackDao.insertRecentTrack(any())).thenAnswer { }
        whenever(recentTrackDao.keepOnlyRecent(any())).thenAnswer { }
        
        // 2. ÇALIŞTIRMA
        val result = repository.addRecentTrack(track)
        
        // 3. KONTROL
        assertThat(result.isSuccess).isTrue()
        verify(recentTrackDao).insertRecentTrack(any())
        verify(recentTrackDao).keepOnlyRecent(100)
    }
    
    // TEST 5: Son dinlenen şarkı ekleme - hata durumu
    @Test
    fun `addRecentTrack should handle error correctly`() = runTest {
        // 1. HAZIRLIK
        val track = createTestTrack()
        whenever(recentTrackDao.insertRecentTrack(any())).thenThrow(RuntimeException("Database error"))
        
        // 2. ÇALIŞTIRMA
        val result = repository.addRecentTrack(track)
        
        // 3. KONTROL
        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isInstanceOf(RuntimeException::class.java)
    }
    
    // TEST 6: Tüm son dinlenenleri temizleme
    @Test
    fun `clearRecentTracks should delete all tracks successfully`() = runTest {
        // 1. HAZIRLIK
        val entities = listOf(
            createRecentTrackEntity(id = 1L),
            createRecentTrackEntity(id = 2L)
        )
        whenever(recentTrackDao.getRecentTracksSync(Int.MAX_VALUE)).thenReturn(entities)
        whenever(recentTrackDao.deleteRecentTrackById(any())).thenAnswer { }
        
        // 2. ÇALIŞTIRMA
        val result = repository.clearRecentTracks()
        
        // 3. KONTROL
        assertThat(result.isSuccess).isTrue()
        verify(recentTrackDao).getRecentTracksSync(Int.MAX_VALUE)
        verify(recentTrackDao, times(2)).deleteRecentTrackById(any())
    }
    
    // TEST 7: Tüm son dinlenenleri temizleme - boş liste
    @Test
    fun `clearRecentTracks should handle empty list correctly`() = runTest {
        // 1. HAZIRLIK
        whenever(recentTrackDao.getRecentTracksSync(Int.MAX_VALUE)).thenReturn(emptyList())
        
        // 2. ÇALIŞTIRMA
        val result = repository.clearRecentTracks()
        
        // 3. KONTROL
        assertThat(result.isSuccess).isTrue()
        verify(recentTrackDao).getRecentTracksSync(Int.MAX_VALUE)
        verify(recentTrackDao, never()).deleteRecentTrackById(any())
    }
    
    // TEST 8: Flow testi - getRecentTracks
    @Test
    fun `getRecentTracks flow should emit tracks`() = runTest {
        // 1. HAZIRLIK
        val entity = createRecentTrackEntity()
        whenever(recentTrackDao.getRecentTracks(50)).thenReturn(flowOf(listOf(entity)))
        
        // 2. ÇALIŞTIRMA & KONTROL (Turbine ile)
        repository.getRecentTracks(50).test {
            val tracks = awaitItem()
            assertThat(tracks).hasSize(1)
            assertThat(tracks[0].id).isEqualTo(1L)
            awaitComplete()
        }
    }
    
    // TEST 9: Multiple tracks in recent list
    @Test
    fun `getRecentTracks should return multiple tracks in correct order`() = runTest {
        // 1. HAZIRLIK
        val entities = listOf(
            createRecentTrackEntity(id = 1L, title = "Track 1"),
            createRecentTrackEntity(id = 2L, title = "Track 2"),
            createRecentTrackEntity(id = 3L, title = "Track 3")
        )
        whenever(recentTrackDao.getRecentTracks(50)).thenReturn(flowOf(entities))
        
        // 2. ÇALIŞTIRMA
        val result = repository.getRecentTracks(50).first()
        
        // 3. KONTROL
        assertThat(result).hasSize(3)
        assertThat(result[0].id).isEqualTo(1L)
        assertThat(result[1].id).isEqualTo(2L)
        assertThat(result[2].id).isEqualTo(3L)
    }
    
    // HELPER FONKSİYONLAR
    
    private fun createTestTrack() = Track(
        id = 1L,
        title = "Test Track",
        titleShort = "Test",
        duration = 180,
        previewUrl = "https://example.com/preview.mp3",
        artist = Artist(
            id = 1L,
            name = "Test Artist",
            picture = null,
            pictureSmall = null,
            pictureMedium = null,
            pictureBig = null,
            pictureXl = null
        ),
        album = Album(
            id = 1L,
            title = "Test Album",
            cover = null,
            coverSmall = null,
            coverMedium = null,
            coverBig = null,
            coverXl = null,
            artist = Artist(
                id = 1L,
                name = "Test Artist",
                picture = null,
                pictureSmall = null,
                pictureMedium = null,
                pictureBig = null,
                pictureXl = null
            ),
            tracks = emptyList()
        )
    )
    
    private fun createRecentTrackEntity(
        id: Long = 1L,
        title: String = "Test Track"
    ) = RecentTrackEntity(
        trackId = id,
        title = title,
        titleShort = "Test",
        duration = 180,
        previewUrl = "https://example.com/preview.mp3",
        artistId = 1L,
        artistName = "Test Artist",
        albumId = 1L,
        albumTitle = "Test Album",
        albumCover = null,
        playedAt = System.currentTimeMillis()
    )
}

