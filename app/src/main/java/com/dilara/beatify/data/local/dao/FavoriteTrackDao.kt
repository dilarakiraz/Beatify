package com.dilara.beatify.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dilara.beatify.data.local.entity.FavoriteTrackEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteTrackDao {
    
    @Query("SELECT * FROM favorite_tracks ORDER BY addedAt DESC")
    fun getAllFavoriteTracks(): Flow<List<FavoriteTrackEntity>>
    
    @Query("SELECT * FROM favorite_tracks WHERE trackId = :trackId")
    suspend fun getFavoriteTrackById(trackId: Long): FavoriteTrackEntity?
    
    @Query("SELECT trackId FROM favorite_tracks")
    suspend fun getAllFavoriteTrackIds(): List<Long>
    
    @Query("SELECT EXISTS(SELECT 1 FROM favorite_tracks WHERE trackId = :trackId)")
    fun isFavorite(trackId: Long): Flow<Boolean>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteTrack(track: FavoriteTrackEntity)
    
    @Delete
    suspend fun deleteFavoriteTrack(track: FavoriteTrackEntity)
    
    @Query("DELETE FROM favorite_tracks WHERE trackId = :trackId")
    suspend fun deleteFavoriteTrackById(trackId: Long)
}


