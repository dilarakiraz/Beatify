package com.dilara.beatify.domain.repository

import com.dilara.beatify.domain.model.Track
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for favorites operations
 * Domain layer - defines contracts without implementation details
 */
interface FavoritesRepository {
    
    /**
     * Get all favorite tracks
     */
    fun getAllFavorites(): Flow<List<Track>>
    
    /**
     * Check if a track is favorite
     */
    fun isFavorite(trackId: Long): Flow<Boolean>
    
    /**
     * Add track to favorites
     */
    suspend fun addToFavorites(track: Track): Result<Unit>
    
    /**
     * Remove track from favorites
     */
    suspend fun removeFromFavorites(trackId: Long): Result<Unit>
    
    /**
     * Toggle favorite status
     */
    suspend fun toggleFavorite(track: Track): Result<Boolean>
}


