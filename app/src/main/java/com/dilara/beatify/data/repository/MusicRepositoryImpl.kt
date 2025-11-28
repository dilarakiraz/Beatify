package com.dilara.beatify.data.repository

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
        return try {
            val response = apiService.getTopTracks()
            Result.success(response.tracks.map { it.toDomain() })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun searchTracks(query: String, limit: Int, index: Int): Result<List<Track>> {
        return try {
            val response = apiService.search(query, limit, index)
            Result.success(response.tracks.map { it.toDomain() })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getTrackById(trackId: Long): Result<Track> {
        return try {
            val response = apiService.getTrack(trackId)
            Result.success(response.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getArtistById(artistId: Long): Result<Artist> {
        return try {
            val response = apiService.getArtist(artistId)
            Result.success(response.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getAlbumById(albumId: Long): Result<Album> {
        return try {
            val response = apiService.getAlbum(albumId)
            Result.success(response.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

