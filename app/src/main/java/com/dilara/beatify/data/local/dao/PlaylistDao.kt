package com.dilara.beatify.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dilara.beatify.data.local.entity.PlaylistEntity
import com.dilara.beatify.data.local.entity.PlaylistTrackEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaylistDao {
    
    @Query("SELECT * FROM playlists ORDER BY position ASC")
    fun getAllPlaylists(): Flow<List<PlaylistEntity>>
    
    @Query("SELECT * FROM playlists WHERE id = :playlistId")
    suspend fun getPlaylistById(playlistId: Long): PlaylistEntity?
    
    @Query("SELECT COUNT(*) FROM playlist_tracks WHERE playlistId = :playlistId")
    suspend fun getTrackCount(playlistId: Long): Int
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylist(playlist: PlaylistEntity): Long
    
    @Query("UPDATE playlists SET name = :name, updatedAt = :updatedAt WHERE id = :playlistId")
    suspend fun updatePlaylistName(playlistId: Long, name: String, updatedAt: Long)
    
    @Query("UPDATE playlists SET coverUrl = :coverUrl, updatedAt = :updatedAt WHERE id = :playlistId")
    suspend fun updatePlaylistCover(playlistId: Long, coverUrl: String?, updatedAt: Long)
    
    @Delete
    suspend fun deletePlaylist(playlist: PlaylistEntity)
    
    @Query("DELETE FROM playlists WHERE id = :playlistId")
    suspend fun deletePlaylistById(playlistId: Long)
    
    @Query("SELECT * FROM playlist_tracks WHERE playlistId = :playlistId ORDER BY position ASC")
    suspend fun getPlaylistTracks(playlistId: Long): List<PlaylistTrackEntity>
    
    @Query("SELECT COUNT(*) FROM playlist_tracks WHERE playlistId = :playlistId")
    fun getPlaylistTrackCount(playlistId: Long): Flow<Int>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylistTrack(track: PlaylistTrackEntity)
    
    @Query("DELETE FROM playlist_tracks WHERE playlistId = :playlistId AND trackId = :trackId")
    suspend fun removeTrackFromPlaylist(playlistId: Long, trackId: Long)
    
    @Query("DELETE FROM playlist_tracks WHERE playlistId = :playlistId")
    suspend fun deleteAllTracksFromPlaylist(playlistId: Long)
    
    @Query("SELECT MAX(position) FROM playlist_tracks WHERE playlistId = :playlistId")
    suspend fun getMaxPosition(playlistId: Long): Int?
    
    @Query("UPDATE playlist_tracks SET position = :newPosition WHERE id = :trackId")
    suspend fun updateTrackPosition(trackId: Long, newPosition: Int)
    
    @Query("SELECT MAX(position) FROM playlists")
    suspend fun getMaxPlaylistPosition(): Int?
    
    @Query("UPDATE playlists SET position = :newPosition WHERE id = :playlistId")
    suspend fun updatePlaylistPosition(playlistId: Long, newPosition: Int)
}