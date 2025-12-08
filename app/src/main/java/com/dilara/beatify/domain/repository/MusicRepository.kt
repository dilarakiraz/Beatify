package com.dilara.beatify.domain.repository

import com.dilara.beatify.domain.model.Album
import com.dilara.beatify.domain.model.Artist
import com.dilara.beatify.domain.model.Track
import com.dilara.beatify.domain.model.Genre
import com.dilara.beatify.domain.model.Radio

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
     * Get top albums from charts
     */
    suspend fun getTopAlbums(): Result<List<Album>>
    
    /**
     * Get top artists from charts
     */
    suspend fun getTopArtists(): Result<List<Artist>>
    
    /**
     * Search for tracks
     */
    suspend fun searchTracks(query: String, limit: Int = 25, index: Int = 0): Result<List<Track>>
    
    /**
     * Search for artists
     */
    suspend fun searchArtists(query: String, limit: Int = 25, index: Int = 0): Result<List<Artist>>
    
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
    
    /**
     * Get artist's albums
     */
    suspend fun getArtistAlbums(artistId: Long, limit: Int = 25, index: Int = 0): Result<List<Album>>
    
    /**
     * Get artist's top tracks
     */
    suspend fun getArtistTopTracks(artistId: Long, limit: Int = 25): Result<List<Track>>
    
    /**
     * Get related artists
     */
    suspend fun getRelatedArtists(artistId: Long, limit: Int = 10): Result<List<Artist>>
    
    /**
     * Get list of genres
     */
    suspend fun getGenres(): Result<List<Genre>>
    
    /**
     * Get artists by genre
     */
    suspend fun getGenreArtists(genreId: Long, limit: Int = 25, index: Int = 0): Result<List<Artist>>
    
    /**
     * Get radio tracks from genre
     */
    suspend fun getGenreRadio(genreId: Long, limit: Int = 25): Result<List<Track>>
    
    /**
     * Get list of radio stations
     */
    suspend fun getRadios(): Result<List<Radio>>
    
    /**
     * Get tracks from radio station
     */
    suspend fun getRadioTracks(radioId: Long, limit: Int = 40): Result<List<Track>>
}

