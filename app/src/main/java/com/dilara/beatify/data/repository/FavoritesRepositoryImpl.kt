package com.dilara.beatify.data.repository

import com.dilara.beatify.data.local.dao.FavoriteTrackDao
import com.dilara.beatify.data.mapper.toDomain
import com.dilara.beatify.data.mapper.toFavoriteEntity
import com.dilara.beatify.domain.model.Track
import com.dilara.beatify.domain.repository.FavoritesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FavoritesRepositoryImpl @Inject constructor(
    private val favoriteTrackDao: FavoriteTrackDao
) : FavoritesRepository {
    
    override fun getAllFavorites(): Flow<List<Track>> {
        return favoriteTrackDao.getAllFavoriteTracks().map { entities ->
            entities.map { it.toDomain() }
        }
    }
    
    override fun isFavorite(trackId: Long): Flow<Boolean> {
        return favoriteTrackDao.isFavorite(trackId)
    }
    
    override suspend fun addToFavorites(track: Track): Result<Unit> {
        return try {
            favoriteTrackDao.insertFavoriteTrack(track.toFavoriteEntity())
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun removeFromFavorites(trackId: Long): Result<Unit> {
        return try {
            favoriteTrackDao.deleteFavoriteTrackById(trackId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun toggleFavorite(track: Track): Result<Boolean> {
        return try {
            val existing = favoriteTrackDao.getFavoriteTrackById(track.id)
            if (existing != null) {
                favoriteTrackDao.deleteFavoriteTrackById(track.id)
                Result.success(false)
            } else {
                favoriteTrackDao.insertFavoriteTrack(track.toFavoriteEntity())
                Result.success(true)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

