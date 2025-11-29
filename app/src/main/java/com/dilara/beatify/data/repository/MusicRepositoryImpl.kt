package com.dilara.beatify.data.repository

import com.dilara.beatify.core.utils.safeApiCall
import com.dilara.beatify.data.mapper.toDomain
import com.dilara.beatify.data.remote.api.DeezerApiService
import com.dilara.beatify.domain.model.Album
import com.dilara.beatify.domain.model.Artist
import com.dilara.beatify.domain.model.Track
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
            transform = { response -> response.tracks.map { it.toDomain() } }
        )
    }

    override suspend fun searchTracks(query: String, limit: Int, index: Int): Result<List<Track>> {
        return safeApiCall(
            apiCall = { apiService.search(query, limit, index) },
            transform = { response -> response.tracks.map { it.toDomain() } }
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
}

