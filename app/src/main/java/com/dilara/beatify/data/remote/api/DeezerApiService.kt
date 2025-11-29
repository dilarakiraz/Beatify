package com.dilara.beatify.data.remote.api

import com.dilara.beatify.data.remote.model.DeezerSearchResponse
import com.dilara.beatify.data.remote.model.DeezerTrackResponse
import com.dilara.beatify.data.remote.model.DeezerArtistResponse
import com.dilara.beatify.data.remote.model.DeezerAlbumResponse
import com.dilara.beatify.data.remote.model.DeezerChartResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Deezer Public API Service
 * Base URL: https://api.deezer.com/
 */
interface DeezerApiService {
    
    /**
     * Get top tracks from Deezer charts
     * GET /chart/0/tracks
     */
    @GET("chart/0/tracks")
    suspend fun getTopTracks(): DeezerChartResponse
    
    /**
     * Search for tracks, artists, albums
     * GET /search?q={query}
     */
    @GET("search")
    suspend fun search(
        @Query("q") query: String,
        @Query("limit") limit: Int = 25,
        @Query("index") index: Int = 0
    ): DeezerSearchResponse
    
    /**
     * Get track details
     * GET /track/{id}
     */
    @GET("track/{id}")
    suspend fun getTrack(@Path("id") trackId: Long): DeezerTrackResponse
    
    /**
     * Get artist details
     * GET /artist/{id}
     */
    @GET("artist/{id}")
    suspend fun getArtist(@Path("id") artistId: Long): DeezerArtistResponse
    
    /**
     * Get album details
     * GET /album/{id}
     */
    @GET("album/{id}")
    suspend fun getAlbum(@Path("id") albumId: Long): DeezerAlbumResponse
}


