package com.dilara.beatify.domain.repository

import com.dilara.beatify.domain.model.Track
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for recent tracks operations
 * Domain layer - defines contracts without implementation details
 */
interface RecentTracksRepository {
    
    /**
     * Get recent tracks
     */
    fun getRecentTracks(limit: Int = 50): Flow<List<Track>>
    
    /**
     * Add track to recent tracks
     */
    suspend fun addRecentTrack(track: Track): Result<Unit>
    
    /**
     * Clear all recent tracks
     */
    suspend fun clearRecentTracks(): Result<Unit>
}