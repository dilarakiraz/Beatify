package com.dilara.beatify.data.repository

import com.dilara.beatify.core.utils.safeApiCall

import com.dilara.beatify.data.local.dao.PlaylistDao
import com.dilara.beatify.data.mapper.toDomain
import com.dilara.beatify.data.mapper.toPlaylistTrackEntity
import com.dilara.beatify.domain.model.Playlist
import com.dilara.beatify.domain.model.Track
import com.dilara.beatify.domain.repository.PlaylistRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlaylistRepositoryImpl @Inject constructor(
    private val playlistDao: PlaylistDao
) : PlaylistRepository {
    
    override fun getAllPlaylists(): Flow<List<Playlist>> {
        return playlistDao.getAllPlaylists().map { playlists ->
            playlists.map { playlistEntity ->
                playlistEntity.toDomain(trackCount = 0)
            }
        }
    }
    
    override suspend fun getPlaylistById(playlistId: Long): Playlist? {
        val playlist = playlistDao.getPlaylistById(playlistId) ?: return null
        val trackCount = playlistDao.getTrackCount(playlistId)
        return playlist.toDomain(trackCount)
    }
    
    override suspend fun createPlaylist(name: String, coverUrl: String?): Result<Long> {
        return safeApiCall {
            val maxPosition = playlistDao.getMaxPlaylistPosition() ?: -1
            val newPosition = maxPosition + 1
            val playlist = com.dilara.beatify.data.local.entity.PlaylistEntity(
                name = name,
                coverUrl = coverUrl,
                position = newPosition
            )
            playlistDao.insertPlaylist(playlist)
        }
    }
    
    override suspend fun updatePlaylistName(playlistId: Long, name: String): Result<Unit> {
        return safeApiCall {
            playlistDao.updatePlaylistName(playlistId, name, System.currentTimeMillis())
        }
    }
    
    override suspend fun updatePlaylistCover(playlistId: Long, coverUrl: String?): Result<Unit> {
        return safeApiCall {
            playlistDao.updatePlaylistCover(playlistId, coverUrl, System.currentTimeMillis())
        }
    }
    
    override suspend fun deletePlaylist(playlistId: Long): Result<Unit> {
        return safeApiCall {
            playlistDao.deletePlaylistById(playlistId)
        }
    }
    
    override suspend fun addTrackToPlaylist(playlistId: Long, track: Track): Result<Unit> {
        return safeApiCall {
            val maxPosition = playlistDao.getMaxPosition(playlistId) ?: -1
            val newPosition = maxPosition + 1
            val playlistTrack = track.toPlaylistTrackEntity(playlistId, newPosition)
            playlistDao.insertPlaylistTrack(playlistTrack)
            
            val playlist = playlistDao.getPlaylistById(playlistId)
            if (playlist?.coverUrl == null) {
                val coverUrl = track.album.coverBig ?: track.album.cover
                playlistDao.updatePlaylistCover(playlistId, coverUrl, System.currentTimeMillis())
            }
        }
    }
    
    override suspend fun removeTrackFromPlaylist(playlistId: Long, trackId: Long): Result<Unit> {
        return safeApiCall {
            playlistDao.removeTrackFromPlaylist(playlistId, trackId)
        }
    }
    
    override suspend fun getPlaylistTracks(playlistId: Long): List<Track> {
        return playlistDao.getPlaylistTracks(playlistId).map { it.toDomain() }
    }
    
    override suspend fun reorderPlaylistTracks(
        playlistId: Long,
        fromIndex: Int,
        toIndex: Int
    ): Result<Unit> {
        return safeApiCall {
            val tracks = playlistDao.getPlaylistTracks(playlistId)
            
            if (fromIndex !in tracks.indices || toIndex !in tracks.indices) {
                throw IllegalArgumentException("Invalid index")
            }
            
            val reorderedTracks = tracks.toMutableList()
            val movedTrack = reorderedTracks.removeAt(fromIndex)
            reorderedTracks.add(toIndex, movedTrack)
            
            reorderedTracks.forEachIndexed { index, track ->
                playlistDao.updateTrackPosition(track.id, index)
            }
        }
    }
    
    override suspend fun reorderPlaylists(fromIndex: Int, toIndex: Int): Result<Unit> {
        return safeApiCall {
            val playlists = playlistDao.getAllPlaylists().first()
            
            if (fromIndex !in playlists.indices || toIndex !in playlists.indices) {
                throw IllegalArgumentException("Invalid index")
            }
            
            val reorderedPlaylists = playlists.toMutableList()
            val movedPlaylist = reorderedPlaylists.removeAt(fromIndex)
            reorderedPlaylists.add(toIndex, movedPlaylist)
            
            reorderedPlaylists.forEachIndexed { index, playlist ->
                playlistDao.updatePlaylistPosition(playlist.id, index)
            }
        }
    }
}

