package com.dilara.beatify.domain.repository

import com.dilara.beatify.domain.model.Playlist
import com.dilara.beatify.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {
    fun getAllPlaylists(): Flow<List<Playlist>>
    suspend fun getPlaylistById(playlistId: Long): Playlist?
    suspend fun createPlaylist(name: String, coverUrl: String?): Result<Long>
    suspend fun updatePlaylistName(playlistId: Long, name: String): Result<Unit>
    suspend fun updatePlaylistCover(playlistId: Long, coverUrl: String?): Result<Unit>
    suspend fun deletePlaylist(playlistId: Long): Result<Unit>
    suspend fun addTrackToPlaylist(playlistId: Long, track: Track): Result<Unit>
    suspend fun removeTrackFromPlaylist(playlistId: Long, trackId: Long): Result<Unit>
    suspend fun getPlaylistTracks(playlistId: Long): List<Track>
}


