package com.dilara.beatify.data.repository

import com.dilara.beatify.core.utils.safeApiCall

import com.dilara.beatify.data.local.dao.FavoriteTrackDao
import com.dilara.beatify.data.mapper.toDomain
import com.dilara.beatify.data.mapper.toFavoriteEntity
import com.dilara.beatify.domain.model.Track
import com.dilara.beatify.domain.repository.FavoritesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FavoritesRepositoryImpl @Inject constructor(
    private val favoriteTrackDao: FavoriteTrackDao
) : FavoritesRepository {
    
    override fun getAllFavorites(): Flow<List<Track>> {
        return favoriteTrackDao.getAllFavoriteTracks().map { entities ->
            entities.mapNotNull { entity ->
                try {
                    entity.toDomain()
                } catch (e: Exception) {
                    e.printStackTrace()
                    null
                }
            }
        }
    }
    
    override fun isFavorite(trackId: Long): Flow<Boolean> {
        return favoriteTrackDao.isFavorite(trackId)
    }
    
    override suspend fun addToFavorites(track: Track): Result<Unit> {
        return safeApiCall {
            val maxPosition = favoriteTrackDao.getMaxPosition() ?: -1
            val newPosition = maxPosition + 1
            favoriteTrackDao.insertFavoriteTrack(track.toFavoriteEntity(newPosition))
        }
    }
    
    override suspend fun removeFromFavorites(trackId: Long): Result<Unit> {
        return safeApiCall {
            favoriteTrackDao.deleteFavoriteTrackById(trackId)
        }
    }
    
    override suspend fun toggleFavorite(track: Track): Result<Boolean> {
        return safeApiCall {
            val existing = favoriteTrackDao.getFavoriteTrackById(track.id)
            if (existing != null) {
                favoriteTrackDao.deleteFavoriteTrackById(track.id)
                false
            } else {
                val maxPosition = favoriteTrackDao.getMaxPosition() ?: -1
                val newPosition = maxPosition + 1
                favoriteTrackDao.insertFavoriteTrack(track.toFavoriteEntity(newPosition))
                true
            }
        }
    }
    
    override suspend fun reorderFavorites(fromIndex: Int, toIndex: Int): Result<Unit> {
        return safeApiCall {
            val favorites = favoriteTrackDao.getAllFavoriteTracks().first()
            
            if (fromIndex !in favorites.indices || toIndex !in favorites.indices) {
                throw IllegalArgumentException("Invalid index")
            }
            
            val reorderedFavorites = favorites.toMutableList()
            val movedFavorite = reorderedFavorites.removeAt(fromIndex)
            reorderedFavorites.add(toIndex, movedFavorite)
            
            reorderedFavorites.forEachIndexed { index, favorite ->
                favoriteTrackDao.updateTrackPosition(favorite.trackId, index)
            }
        }
    }
}


