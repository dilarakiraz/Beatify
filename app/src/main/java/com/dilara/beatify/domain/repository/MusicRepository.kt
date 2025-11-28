package com.dilara.beatify.domain.repository

import com.dilara.beatify.domain.model.Album
import com.dilara.beatify.domain.model.Artist
import com.dilara.beatify.domain.model.Track

/**
 * Repository interface for music data operations
 * Domain layer - defines contracts without implementation details
 */
interface MusicRepository {
    
    /**
     * Get top tracks from charts
     */
    suspend fun getTopTracks(): Result<List<Track>>
    
    /**
     * Search for tracks
     */
    suspend fun searchTracks(query: String, limit: Int = 25, index: Int = 0): Result<List<Track>>
    
    /**
     * Get track details by ID
     */
    suspend fun getTrackById(trackId: Long): Result<Track>
    
    /**
     * Get artist details by ID
     */
    suspend fun getArtistById(artistId: Long): Result<Artist>
    
    /**
     * Get album details by ID
     */
    suspend fun getAlbumById(albumId: Long): Result<Album>
}

