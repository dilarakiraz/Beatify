package com.dilara.beatify.data.repository

import com.dilara.beatify.core.utils.safeApiCall
import com.dilara.beatify.data.mapper.toDomain
import com.dilara.beatify.data.remote.api.DeezerApiService
import com.dilara.beatify.domain.model.Album
import com.dilara.beatify.domain.model.Artist
import com.dilara.beatify.domain.model.Track
import com.dilara.beatify.domain.model.Genre
import com.dilara.beatify.domain.model.Radio
import com.dilara.beatify.domain.model.PublicPlaylist
import com.dilara.beatify.domain.repository.MusicRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MusicRepositoryImpl @Inject constructor(
    private val apiService: DeezerApiService
) : MusicRepository {

    override suspend fun getTopTracks(): Result<List<Track>> {
        return safeApiCall(
            apiCall = { apiService.getTopTracks() },
            transform = { response -> 
                response.tracks
                    .filter { it.previewUrl != null && it.previewUrl.isNotEmpty() }
                    .map { it.toDomain() } 
            }
        )
    }

    override suspend fun getTopAlbums(): Result<List<Album>> {
        return safeApiCall(
            apiCall = { apiService.getTopAlbums() },
            transform = { response ->
                response.albums.mapNotNull { albumDto ->
                    albumDto.artist?.toDomain()?.let { artist ->
                        albumDto.toDomain(artist = artist)
                    }
                }
            }
        )
    }

    override suspend fun getTopArtists(): Result<List<Artist>> {
        return safeApiCall(
            apiCall = { apiService.getTopArtists() },
            transform = { response -> response.artists.map { it.toDomain() } }
        )
    }
    
    override suspend fun getTopPlaylists(): Result<List<PublicPlaylist>> {
        return safeApiCall(
            apiCall = { apiService.getTopPlaylists() },
            transform = { response -> response.playlists.map { it.toDomain() } }
        )
    }

    override suspend fun searchTracks(query: String, limit: Int, index: Int): Result<List<Track>> {
        return safeApiCall(
            apiCall = { apiService.search(query, limit, index) },
            transform = { response -> response.tracks.map { it.toDomain() } }
        )
    }
    
    override suspend fun searchArtists(query: String, limit: Int, index: Int): Result<List<Artist>> {
        return safeApiCall(
            apiCall = { apiService.searchArtists(query, limit, index) },
            transform = { response -> response.artists.map { it.toDomain() } }
        )
    }
    
    override suspend fun searchAlbums(query: String, limit: Int, index: Int): Result<List<Album>> {
        return safeApiCall(
            apiCall = { apiService.searchAlbums(query, limit, index) },
            transform = { response ->
                response.albums.mapNotNull { albumDto ->
                    albumDto.artist?.toDomain()?.let { artist ->
                        albumDto.toDomain(artist = artist)
                    }
                }
            }
        )
    }
    
    override suspend fun searchPlaylists(query: String, limit: Int, index: Int): Result<List<PublicPlaylist>> {
        return safeApiCall(
            apiCall = { apiService.searchPlaylists(query, limit, index) },
            transform = { response -> response.playlists.map { it.toDomain() } }
        )
    }

    override suspend fun getTrackById(trackId: Long): Result<Track> {
        return safeApiCall(
            apiCall = { apiService.getTrack(trackId) },
            transform = { it.toDomain() }
        )
    }

    override suspend fun getArtistById(artistId: Long): Result<Artist> {
        return safeApiCall(
            apiCall = { apiService.getArtist(artistId) },
            transform = { it.toDomain() }
        )
    }

    override suspend fun getAlbumById(albumId: Long): Result<Album> {
        return safeApiCall(
            apiCall = { apiService.getAlbum(albumId) },
            transform = { it.toDomain() }
        )
    }
    
    override suspend fun getArtistAlbums(artistId: Long, limit: Int, index: Int): Result<List<Album>> {
        val artistResult = getArtistById(artistId)
        val artist = artistResult.getOrNull()
            ?: return Result.failure(Exception("Artist not found"))
        
        return safeApiCall(
            apiCall = { apiService.getArtistAlbums(artistId, limit, index) },
            transform = { response ->
                response.albums.map { albumDto ->
                    albumDto.toDomain(artist = artist)
                }
            }
        )
    }
    
    override suspend fun getArtistTopTracks(artistId: Long, limit: Int): Result<List<Track>> {
        return safeApiCall(
            apiCall = { apiService.getArtistTopTracks(artistId, limit) },
            transform = { response -> response.tracks.map { it.toDomain() } }
        )
    }
    
    override suspend fun getRelatedArtists(artistId: Long, limit: Int): Result<List<Artist>> {
        return safeApiCall(
            apiCall = { apiService.getRelatedArtists(artistId, limit) },
            transform = { response -> response.artists.map { it.toDomain() } }
        )
    }
    
    override suspend fun getGenres(): Result<List<Genre>> {
        return safeApiCall(
            apiCall = { apiService.getGenres() },
            transform = { response -> response.genres.map { it.toDomain() } }
        )
    }
    
    override suspend fun getGenreArtists(genreId: Long, limit: Int, index: Int): Result<List<Artist>> {
        return safeApiCall(
            apiCall = { apiService.getGenreArtists(genreId, limit, index) },
            transform = { response -> response.artists.map { it.toDomain() } }
        )
    }
    
    override suspend fun getGenreRadio(genreId: Long, limit: Int): Result<List<Track>> {
        return safeApiCall(
            apiCall = { apiService.getGenreRadio(genreId, limit) },
            transform = { response -> 
                response.tracks
                    .filter { it.previewUrl != null && it.previewUrl.isNotEmpty() }
                    .map { it.toDomain() } 
            }
        )
    }
    
    override suspend fun getRadios(): Result<List<Radio>> {
        return safeApiCall(
            apiCall = { apiService.getRadios() },
            transform = { response -> response.radios.map { it.toDomain() } }
        )
    }
    
    override suspend fun getRadioTracks(radioId: Long, limit: Int): Result<List<Track>> {
        return safeApiCall(
            apiCall = { apiService.getRadioTracks(radioId, limit) },
            transform = { response -> 
                response.tracks
                    .filter { it.previewUrl != null && it.previewUrl.isNotEmpty() }
                    .map { it.toDomain() } 
            }
        )
    }
    
    override suspend fun getPublicPlaylist(playlistId: Long): Result<PublicPlaylist> {
        return safeApiCall(
            apiCall = { apiService.getPlaylist(playlistId) },
            transform = { it.toDomain() }
        )
    }
    
    override suspend fun getPublicPlaylistTracks(playlistId: Long, limit: Int): Result<List<Track>> {
        return safeApiCall(
            apiCall = { apiService.getPlaylistTracks(playlistId, limit) },
            transform = { response -> 
                response.tracks
                    .filter { it.previewUrl != null && it.previewUrl.isNotEmpty() }
                    .map { it.toDomain() } 
            }
        )
    }
}

