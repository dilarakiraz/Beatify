package com.dilara.beatify.data.repository

import app.cash.turbine.test
import com.dilara.beatify.data.local.dao.FavoriteTrackDao
import com.dilara.beatify.data.local.entity.FavoriteTrackEntity
import com.dilara.beatify.domain.model.Album
import com.dilara.beatify.domain.model.Artist
import com.dilara.beatify.domain.model.Track
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class FavoritesRepositoryImplTest {

    // Mock: Sahte veritabanı (gerçek veritabanı kullanmıyoruz)
    private lateinit var favoriteTrackDao: FavoriteTrackDao
    private lateinit var repository: FavoritesRepositoryImpl

    @Before
    fun setup() {
        // Her test öncesi yeni mock oluştur
        favoriteTrackDao = mock()
        repository = FavoritesRepositoryImpl(favoriteTrackDao)
    }

    // TEST 1: Favorilere ekleme
    @Test
    fun `toggleFavorite - şarkı favorilerde yoksa ekle ve true döndür`() = runTest {
        // 1. HAZIRLIK (Given)
        val track = createTestTrack()

        // Veritabanında bu şarkı YOK diye simüle et
        whenever(favoriteTrackDao.getFavoriteTrackById(1L)).thenReturn(null)
        whenever(favoriteTrackDao.getMaxPosition()).thenReturn(0)
        whenever(favoriteTrackDao.insertFavoriteTrack(any())).thenAnswer { }

        // 2. ÇALIŞTIRMA (When)
        val result = repository.toggleFavorite(track)

        // 3. KONTROL (Then)
        assertThat(result.isSuccess).isTrue()  //  Başarılı mı?
        assertThat(result.getOrNull()).isTrue() //true döndü mü? (eklendi)

        // Veritabanına ekleme çağrısı yapıldı mı kontrol et
        verify(favoriteTrackDao).insertFavoriteTrack(any())
        verify(favoriteTrackDao, never()).deleteFavoriteTrackById(any())
    }

    // TEST 2: Favorilerden çıkarma
    @Test
    fun `toggleFavorite - şarkı favorilerde varsa çıkar ve false döndür`() = runTest {
        // 1. HAZIRLIK
        val track = createTestTrack()
        val existingEntity = createFavoriteTrackEntity()

        // Veritabanında bu şarkı VAR diye simüle et
        whenever(favoriteTrackDao.getFavoriteTrackById(1L)).thenReturn(existingEntity)
        whenever(favoriteTrackDao.deleteFavoriteTrackById(1L)).thenAnswer { }

        // 2. ÇALIŞTIRMA
        val result = repository.toggleFavorite(track)

        // 3. KONTROL
        assertThat(result.isSuccess).isTrue()   //  Başarılı mı?
        assertThat(result.getOrNull()).isFalse() //  false döndü mü? (çıkarıldı)

        // Silme çağrısı yapıldı mı kontrol et
        verify(favoriteTrackDao).deleteFavoriteTrackById(1L)
        verify(favoriteTrackDao, never()).insertFavoriteTrack(any())
    }

    // TEST 3: Favorileri listeleme
    @Test
    fun `getAllFavorites should return list of tracks`() = runTest {
        // 1. HAZIRLIK
        val entity = createFavoriteTrackEntity()
        whenever(favoriteTrackDao.getAllFavoriteTracks()).thenReturn(flowOf(listOf(entity)))

        // 2. ÇALIŞTIRMA
        val result = repository.getAllFavorites().first()

        // 3. KONTROL
        assertThat(result).isNotEmpty()
        assertThat(result.size).isEqualTo(1)
        assertThat(result[0].id).isEqualTo(1L)
        assertThat(result[0].title).isEqualTo("Test Track")
    }

    // TEST 4: Favori kontrolü
    @Test
    fun `isFavorite should return true when track is favorite`() = runTest {
        // 1. HAZIRLIK
        whenever(favoriteTrackDao.isFavorite(1L)).thenReturn(flowOf(true))

        // 2. ÇALIŞTIRMA
        val result = repository.isFavorite(1L).first()

        // 3. KONTROL
        assertThat(result).isTrue()
    }

    // TEST 5: Favorilere ekleme
    @Test
    fun `addToFavorites should insert track successfully`() = runTest {
        // 1. HAZIRLIK
        val track = createTestTrack()
        whenever(favoriteTrackDao.getMaxPosition()).thenReturn(0)
        whenever(favoriteTrackDao.insertFavoriteTrack(any())).thenAnswer { }

        // 2. ÇALIŞTIRMA
        val result = repository.addToFavorites(track)

        // 3. KONTROL
        assertThat(result.isSuccess).isTrue()
        verify(favoriteTrackDao).insertFavoriteTrack(any())
    }

    // TEST 6: Favorilerden çıkarma
    @Test
    fun `removeFromFavorites should delete track successfully`() = runTest {
        // 1. HAZIRLIK
        whenever(favoriteTrackDao.deleteFavoriteTrackById(1L)).thenAnswer { }

        // 2. ÇALIŞTIRMA
        val result = repository.removeFromFavorites(1L)

        // 3. KONTROL
        assertThat(result.isSuccess).isTrue()
        verify(favoriteTrackDao).deleteFavoriteTrackById(1L)
    }

    // TEST 7: Sıralama (Reorder)
    @Test
    fun `reorderFavorites should update positions correctly`() = runTest {
        // 1. HAZIRLIK
        val entities = listOf(
            createFavoriteTrackEntity(id = 1L, position = 0),
            createFavoriteTrackEntity(id = 2L, position = 1),
            createFavoriteTrackEntity(id = 3L, position = 2)
        )
        whenever(favoriteTrackDao.getAllFavoriteTracks()).thenReturn(flowOf(entities))
        whenever(favoriteTrackDao.updateTrackPosition(any(), any())).thenAnswer { }

        // 2. ÇALIŞTIRMA
        val result = repository.reorderFavorites(fromIndex = 0, toIndex = 2)

        // 3. KONTROL
        assertThat(result.isSuccess).isTrue()
        // 3 şarkı için position güncellemesi yapıldı mı?
        verify(favoriteTrackDao, times(3)).updateTrackPosition(any(), any())
    }

    // TEST 8: Hata durumu - geçersiz index
    @Test
    fun `reorderFavorites should fail with invalid index`() = runTest {
        // 1. HAZIRLIK
        val entities = listOf(createFavoriteTrackEntity())
        whenever(favoriteTrackDao.getAllFavoriteTracks()).thenReturn(flowOf(entities))

        // 2. ÇALIŞTIRMA
        val result = repository.reorderFavorites(fromIndex = 0, toIndex = 5)

        // 3. KONTROL
        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isInstanceOf(IllegalArgumentException::class.java)
    }

    // TEST 9: Flow testi (Turbine ile)
    @Test
    fun `getAllFavorites flow should emit tracks`() = runTest {
        // 1. HAZIRLIK
        val entity = createFavoriteTrackEntity()
        whenever(favoriteTrackDao.getAllFavoriteTracks()).thenReturn(flowOf(listOf(entity)))

        // 2. ÇALIŞTIRMA & KONTROL (Turbine ile)
        repository.getAllFavorites().test {
            val tracks = awaitItem()
            assertThat(tracks).hasSize(1)
            assertThat(tracks[0].id).isEqualTo(1L)
            awaitComplete()
        }
    }

    // TEST 10: Boş favoriler listesi
    @Test
    fun `getAllFavorites should return empty list when no favorites`() = runTest {
        // 1. HAZIRLIK
        whenever(favoriteTrackDao.getAllFavoriteTracks()).thenReturn(flowOf(emptyList()))

        // 2. ÇALIŞTIRMA
        val result = repository.getAllFavorites().first()

        // 3. KONTROL
        assertThat(result).isEmpty()
    }

    // TEST 11: isFavorite should return false when track is not favorite
    @Test
    fun `isFavorite should return false when track is not favorite`() = runTest {
        // 1. HAZIRLIK
        whenever(favoriteTrackDao.isFavorite(1L)).thenReturn(flowOf(false))

        // 2. ÇALIŞTIRMA
        val result = repository.isFavorite(1L).first()

        // 3. KONTROL
        assertThat(result).isFalse()
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

    private fun createFavoriteTrackEntity(
        id: Long = 1L,
        position: Int = 0
    ) = FavoriteTrackEntity(
        trackId = id,
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

