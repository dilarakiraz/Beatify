package com.dilara.beatify.data.repository

import app.cash.turbine.test
import com.dilara.beatify.data.local.dao.PlaylistDao
import com.dilara.beatify.data.local.entity.PlaylistEntity
import com.dilara.beatify.data.local.entity.PlaylistTrackEntity
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

class PlaylistRepositoryImplTest {
    
    // Mock: Sahte veritabanı
    private lateinit var playlistDao: PlaylistDao
    private lateinit var repository: PlaylistRepositoryImpl
    
    @Before
    fun setup() {
        // Her test öncesi yeni mock oluştur
        playlistDao = mock()
        repository = PlaylistRepositoryImpl(playlistDao)
    }
    
    // TEST 1: Tüm playlistleri getirme
    @Test
    fun `getAllPlaylists should return list of playlists`() = runTest {
        // 1. HAZIRLIK
        val entity = createPlaylistEntity()
        whenever(playlistDao.getAllPlaylists()).thenReturn(flowOf(listOf(entity)))
        
        // 2. ÇALIŞTIRMA
        val result = repository.getAllPlaylists().first()
        
        // 3. KONTROL
        assertThat(result).isNotEmpty()
        assertThat(result.size).isEqualTo(1)
        assertThat(result[0].id).isEqualTo(1L)
        assertThat(result[0].name).isEqualTo("Test Playlist")
    }
    
    // TEST 2: Boş playlist listesi
    @Test
    fun `getAllPlaylists should return empty list when no playlists`() = runTest {
        // 1. HAZIRLIK
        whenever(playlistDao.getAllPlaylists()).thenReturn(flowOf(emptyList()))
        
        // 2. ÇALIŞTIRMA
        val result = repository.getAllPlaylists().first()
        
        // 3. KONTROL
        assertThat(result).isEmpty()
    }
    
    // TEST 3: Playlist ID ile getirme
    @Test
    fun `getPlaylistById should return playlist when exists`() = runTest {
        // 1. HAZIRLIK
        val entity = createPlaylistEntity(id = 1L)
        whenever(playlistDao.getPlaylistById(1L)).thenReturn(entity)
        whenever(playlistDao.getTrackCount(1L)).thenReturn(5)
        
        // 2. ÇALIŞTIRMA
        val result = repository.getPlaylistById(1L)
        
        // 3. KONTROL
        assertThat(result).isNotNull()
        assertThat(result?.id).isEqualTo(1L)
        assertThat(result?.name).isEqualTo("Test Playlist")
        assertThat(result?.trackCount).isEqualTo(5)
    }
    
    // TEST 4: Playlist ID ile getirme - bulunamadı
    @Test
    fun `getPlaylistById should return null when playlist not exists`() = runTest {
        // 1. HAZIRLIK
        whenever(playlistDao.getPlaylistById(999L)).thenReturn(null)
        
        // 2. ÇALIŞTIRMA
        val result = repository.getPlaylistById(999L)
        
        // 3. KONTROL
        assertThat(result).isNull()
    }
    
    // TEST 5: Playlist oluşturma
    @Test
    fun `createPlaylist should insert playlist successfully`() = runTest {
        // 1. HAZIRLIK
        val playlistName = "My New Playlist"
        val coverUrl = "https://example.com/cover.jpg"
        whenever(playlistDao.getMaxPlaylistPosition()).thenReturn(0)
        whenever(playlistDao.insertPlaylist(any())).thenAnswer { 
            val playlist = it.arguments[0] as PlaylistEntity
            playlist.copy(id = 1L)
        }
        
        // 2. ÇALIŞTIRMA
        val result = repository.createPlaylist(playlistName, coverUrl)
        
        // 3. KONTROL
        assertThat(result.isSuccess).isTrue()
        verify(playlistDao).insertPlaylist(any())
    }
    
    // TEST 6: Playlist oluşturma - cover URL olmadan
    @Test
    fun `createPlaylist should work without cover URL`() = runTest {
        // 1. HAZIRLIK
        val playlistName = "My New Playlist"
        whenever(playlistDao.getMaxPlaylistPosition()).thenReturn(0)
        whenever(playlistDao.insertPlaylist(any())).thenAnswer { 
            val playlist = it.arguments[0] as PlaylistEntity
            playlist.copy(id = 1L)
        }
        
        // 2. ÇALIŞTIRMA
        val result = repository.createPlaylist(playlistName, null)
        
        // 3. KONTROL
        assertThat(result.isSuccess).isTrue()
        verify(playlistDao).insertPlaylist(any())
    }
    
    // TEST 7: Playlist ismini güncelleme
    @Test
    fun `updatePlaylistName should update name successfully`() = runTest {
        // 1. HAZIRLIK
        val newName = "Updated Playlist Name"
        whenever(playlistDao.updatePlaylistName(any(), any(), any())).thenAnswer { }
        
        // 2. ÇALIŞTIRMA
        val result = repository.updatePlaylistName(1L, newName)
        
        // 3. KONTROL
        assertThat(result.isSuccess).isTrue()
        verify(playlistDao).updatePlaylistName(any(), any(), any())
    }
    
    // TEST 8: Playlist cover güncelleme
    @Test
    fun `updatePlaylistCover should update cover successfully`() = runTest {
        // 1. HAZIRLIK
        val newCoverUrl = "https://example.com/new-cover.jpg"
        whenever(playlistDao.updatePlaylistCover(any(), any(), any())).thenAnswer { }
        
        // 2. ÇALIŞTIRMA
        val result = repository.updatePlaylistCover(1L, newCoverUrl)
        
        // 3. KONTROL
        assertThat(result.isSuccess).isTrue()
        verify(playlistDao).updatePlaylistCover(any(), any(), any())
    }
    
    // TEST 9: Playlist silme
    @Test
    fun `deletePlaylist should delete playlist successfully`() = runTest {
        // 1. HAZIRLIK
        whenever(playlistDao.deletePlaylistById(1L)).thenAnswer { }
        
        // 2. ÇALIŞTIRMA
        val result = repository.deletePlaylist(1L)
        
        // 3. KONTROL
        assertThat(result.isSuccess).isTrue()
        verify(playlistDao).deletePlaylistById(1L)
    }
    
    // TEST 10: Playlist'e şarkı ekleme
    @Test
    fun `addTrackToPlaylist should add track successfully`() = runTest {
        // 1. HAZIRLIK
        val track = createTestTrack()
        val playlistEntity = createPlaylistEntity(id = 1L, coverUrl = null)
        whenever(playlistDao.getMaxPosition(1L)).thenReturn(0)
        whenever(playlistDao.insertPlaylistTrack(any())).thenAnswer { }
        whenever(playlistDao.getPlaylistById(1L)).thenReturn(playlistEntity)
        whenever(playlistDao.updatePlaylistCover(any(), any(), any())).thenAnswer { }
        
        // 2. ÇALIŞTIRMA
        val result = repository.addTrackToPlaylist(1L, track)
        
        // 3. KONTROL
        assertThat(result.isSuccess).isTrue()
        verify(playlistDao).insertPlaylistTrack(any())
        // Cover yoksa otomatik eklenmeli
        verify(playlistDao).updatePlaylistCover(any(), anyOrNull(), any())
    }
    
    // TEST 11: Playlist'e şarkı ekleme - cover zaten varsa
    @Test
    fun `addTrackToPlaylist should not update cover when cover already exists`() = runTest {
        // 1. HAZIRLIK
        val track = createTestTrack()
        val playlistEntity = createPlaylistEntity(id = 1L, coverUrl = "existing-cover.jpg")
        whenever(playlistDao.getMaxPosition(1L)).thenReturn(0)
        whenever(playlistDao.insertPlaylistTrack(any())).thenAnswer { }
        whenever(playlistDao.getPlaylistById(1L)).thenReturn(playlistEntity)
        
        // 2. ÇALIŞTIRMA
        val result = repository.addTrackToPlaylist(1L, track)
        
        // 3. KONTROL
        assertThat(result.isSuccess).isTrue()
        verify(playlistDao).insertPlaylistTrack(any())
        // Cover varsa güncelleme yapılmamalı
        verify(playlistDao, never()).updatePlaylistCover(any(), anyOrNull(), any())
    }
    
    // TEST 12: Playlist'ten şarkı çıkarma
    @Test
    fun `removeTrackFromPlaylist should remove track successfully`() = runTest {
        // 1. HAZIRLIK
        whenever(playlistDao.removeTrackFromPlaylist(1L, 100L)).thenAnswer { }
        
        // 2. ÇALIŞTIRMA
        val result = repository.removeTrackFromPlaylist(1L, 100L)
        
        // 3. KONTROL
        assertThat(result.isSuccess).isTrue()
        verify(playlistDao).removeTrackFromPlaylist(1L, 100L)
    }
    
    // TEST 13: Playlist şarkılarını getirme
    @Test
    fun `getPlaylistTracks should return list of tracks`() = runTest {
        // 1. HAZIRLIK
        val trackEntity = createPlaylistTrackEntity()
        whenever(playlistDao.getPlaylistTracks(1L)).thenReturn(listOf(trackEntity))
        
        // 2. ÇALIŞTIRMA
        val result = repository.getPlaylistTracks(1L)
        
        // 3. KONTROL
        assertThat(result).isNotEmpty()
        assertThat(result.size).isEqualTo(1)
        assertThat(result[0].id).isEqualTo(1L)
        assertThat(result[0].title).isEqualTo("Test Track")
    }
    
    // TEST 14: Playlist şarkılarını getirme - boş liste
    @Test
    fun `getPlaylistTracks should return empty list when no tracks`() = runTest {
        // 1. HAZIRLIK
        whenever(playlistDao.getPlaylistTracks(1L)).thenReturn(emptyList())
        
        // 2. ÇALIŞTIRMA
        val result = repository.getPlaylistTracks(1L)
        
        // 3. KONTROL
        assertThat(result).isEmpty()
    }
    
    // TEST 15: Playlist içindeki şarkıları sıralama
    @Test
    fun `reorderPlaylistTracks should update positions correctly`() = runTest {
        // 1. HAZIRLIK
        val trackEntities = listOf(
            createPlaylistTrackEntity(id = 1L, position = 0),
            createPlaylistTrackEntity(id = 2L, position = 1),
            createPlaylistTrackEntity(id = 3L, position = 2)
        )
        whenever(playlistDao.getPlaylistTracks(1L)).thenReturn(trackEntities)
        whenever(playlistDao.updateTrackPosition(any(), any())).thenAnswer { }
        
        // 2. ÇALIŞTIRMA
        val result = repository.reorderPlaylistTracks(1L, fromIndex = 0, toIndex = 2)
        
        // 3. KONTROL
        assertThat(result.isSuccess).isTrue()
        // 3 şarkı için position güncellemesi yapıldı mı?
        verify(playlistDao, times(3)).updateTrackPosition(any(), any())
    }
    
    // TEST 16: Playlist içindeki şarkıları sıralama - geçersiz index
    @Test
    fun `reorderPlaylistTracks should fail with invalid index`() = runTest {
        // 1. HAZIRLIK
        val trackEntities = listOf(createPlaylistTrackEntity())
        whenever(playlistDao.getPlaylistTracks(1L)).thenReturn(trackEntities)
        
        // 2. ÇALIŞTIRMA
        val result = repository.reorderPlaylistTracks(1L, fromIndex = 0, toIndex = 5)
        
        // 3. KONTROL
        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isInstanceOf(IllegalArgumentException::class.java)
    }
    
    // TEST 17: Playlistleri sıralama
    @Test
    fun `reorderPlaylists should update positions correctly`() = runTest {
        // 1. HAZIRLIK
        val playlistEntities = listOf(
            createPlaylistEntity(id = 1L, position = 0),
            createPlaylistEntity(id = 2L, position = 1),
            createPlaylistEntity(id = 3L, position = 2)
        )
        whenever(playlistDao.getAllPlaylists()).thenReturn(flowOf(playlistEntities))
        whenever(playlistDao.updatePlaylistPosition(any(), any())).thenAnswer { }
        
        // 2. ÇALIŞTIRMA
        val result = repository.reorderPlaylists(fromIndex = 0, toIndex = 2)
        
        // 3. KONTROL
        assertThat(result.isSuccess).isTrue()
        // 3 playlist için position güncellemesi yapıldı mı?
        verify(playlistDao, times(3)).updatePlaylistPosition(any(), any())
    }
    
    // TEST 18: Playlistleri sıralama - geçersiz index
    @Test
    fun `reorderPlaylists should fail with invalid index`() = runTest {
        // 1. HAZIRLIK
        val playlistEntities = listOf(createPlaylistEntity())
        whenever(playlistDao.getAllPlaylists()).thenReturn(flowOf(playlistEntities))
        
        // 2. ÇALIŞTIRMA
        val result = repository.reorderPlaylists(fromIndex = 0, toIndex = 5)
        
        // 3. KONTROL
        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isInstanceOf(IllegalArgumentException::class.java)
    }
    
    // TEST 19: Flow testi - getAllPlaylists
    @Test
    fun `getAllPlaylists flow should emit playlists`() = runTest {
        // 1. HAZIRLIK
        val entity = createPlaylistEntity()
        whenever(playlistDao.getAllPlaylists()).thenReturn(flowOf(listOf(entity)))
        
        // 2. ÇALIŞTIRMA & KONTROL (Turbine ile)
        repository.getAllPlaylists().test {
            val playlists = awaitItem()
            assertThat(playlists).hasSize(1)
            assertThat(playlists[0].id).isEqualTo(1L)
            awaitComplete()
        }
    }
    
    // HELPER FONKSİYONLAR: Test verileri oluştur
    
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
            cover = "https://example.com/cover.jpg",
            coverSmall = null,
            coverMedium = null,
            coverBig = "https://example.com/cover-big.jpg",
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
    
    private fun createPlaylistEntity(
        id: Long = 1L,
        name: String = "Test Playlist",
        coverUrl: String? = null,
        position: Int = 0
    ) = PlaylistEntity(
        id = id,
        name = name,
        coverUrl = coverUrl,
        position = position,
        createdAt = System.currentTimeMillis(),
        updatedAt = System.currentTimeMillis()
    )
    
    private fun createPlaylistTrackEntity(
        id: Long = 1L,
        playlistId: Long = 1L,
        trackId: Long = 1L,
        position: Int = 0
    ) = PlaylistTrackEntity(
        id = id,
        playlistId = playlistId,
        trackId = trackId,
        title = "Test Track",
        titleShort = "Test",
        duration = 180,
        previewUrl = "https://example.com/preview.mp3",
        artistId = 1L,
        artistName = "Test Artist",
        albumId = 1L,
        albumTitle = "Test Album",
        albumCover = null,
        position = position,
        addedAt = System.currentTimeMillis()
    )
}

