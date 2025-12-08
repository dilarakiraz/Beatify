package com.dilara.beatify.data.remote.api

import com.dilara.beatify.data.remote.model.DeezerSearchResponse
import com.dilara.beatify.data.remote.model.DeezerTrackResponse
import com.dilara.beatify.data.remote.model.DeezerArtistResponse
import com.dilara.beatify.data.remote.model.DeezerAlbumResponse
import com.dilara.beatify.data.remote.model.DeezerChartResponse
import com.dilara.beatify.data.remote.model.DeezerArtistAlbumsResponse
import com.dilara.beatify.data.remote.model.DeezerArtistTopTracksResponse
import com.dilara.beatify.data.remote.model.DeezerRelatedArtistsResponse
import com.dilara.beatify.data.remote.model.DeezerArtistSearchResponse
import com.dilara.beatify.data.remote.model.DeezerChartAlbumsResponse
import com.dilara.beatify.data.remote.model.DeezerChartArtistsResponse
import com.dilara.beatify.data.remote.model.DeezerGenresResponse
import com.dilara.beatify.data.remote.model.DeezerGenreArtistsResponse
import com.dilara.beatify.data.remote.model.DeezerGenreRadioResponse
import com.dilara.beatify.data.remote.model.DeezerRadiosResponse
import com.dilara.beatify.data.remote.model.DeezerRadioTracksResponse
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
     * Get top albums from Deezer charts
     * GET /chart/0/albums
     */
    @GET("chart/0/albums")
    suspend fun getTopAlbums(): DeezerChartAlbumsResponse
    
    /**
     * Get top artists from Deezer charts
     * GET /chart/0/artists
     */
    @GET("chart/0/artists")
    suspend fun getTopArtists(): DeezerChartArtistsResponse
    
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
     * Search for artists
     * GET /search/artist?q={query}
     */
    @GET("search/artist")
    suspend fun searchArtists(
        @Query("q") query: String,
        @Query("limit") limit: Int = 25,
        @Query("index") index: Int = 0
    ): DeezerArtistSearchResponse
    
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
    
    /**
     * Get artist's albums
     * GET /artist/{id}/albums
     */
    @GET("artist/{id}/albums")
    suspend fun getArtistAlbums(
        @Path("id") artistId: Long,
        @Query("limit") limit: Int = 25,
        @Query("index") index: Int = 0
    ): DeezerArtistAlbumsResponse
    
    /**
     * Get artist's top tracks
     * GET /artist/{id}/top
     */
    @GET("artist/{id}/top")
    suspend fun getArtistTopTracks(
        @Path("id") artistId: Long,
        @Query("limit") limit: Int = 25
    ): DeezerArtistTopTracksResponse
    
    /**
     * Get related artists
     * GET /artist/{id}/related
     */
    @GET("artist/{id}/related")
    suspend fun getRelatedArtists(
        @Path("id") artistId: Long,
        @Query("limit") limit: Int = 10
    ): DeezerRelatedArtistsResponse
    
    /**
     * Get list of genres
     * GET /genre
     */
    @GET("genre")
    suspend fun getGenres(): DeezerGenresResponse
    
    /**
     * Get artists by genre
     * GET /genre/{id}/artists
     */
    @GET("genre/{id}/artists")
    suspend fun getGenreArtists(
        @Path("id") genreId: Long,
        @Query("limit") limit: Int = 25,
        @Query("index") index: Int = 0
    ): DeezerGenreArtistsResponse
    
    /**
     * Get radio tracks from genre
     * GET /genre/{id}/radio
     */
    @GET("genre/{id}/radio")
    suspend fun getGenreRadio(
        @Path("id") genreId: Long,
        @Query("limit") limit: Int = 25
    ): DeezerGenreRadioResponse
    
    /**
     * Get list of radio stations
     * GET /radio
     */
    @GET("radio")
    suspend fun getRadios(): DeezerRadiosResponse
    
    /**
     * Get tracks from radio station
     * GET /radio/{id}/tracks
     */
    @GET("radio/{id}/tracks")
    suspend fun getRadioTracks(
        @Path("id") radioId: Long,
        @Query("limit") limit: Int = 40
    ): DeezerRadioTracksResponse
}


