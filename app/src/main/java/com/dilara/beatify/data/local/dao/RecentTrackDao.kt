package com.dilara.beatify.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dilara.beatify.data.local.entity.RecentTrackEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RecentTrackDao {
    
    @Query("SELECT * FROM recent_tracks ORDER BY playedAt DESC LIMIT :limit")
    fun getRecentTracks(limit: Int = 50): Flow<List<RecentTrackEntity>>
    
    @Query("SELECT * FROM recent_tracks ORDER BY playedAt DESC LIMIT :limit")
    suspend fun getRecentTracksSync(limit: Int = 50): List<RecentTrackEntity>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecentTrack(track: RecentTrackEntity)
    
    @Query("DELETE FROM recent_tracks WHERE trackId = :trackId")
    suspend fun deleteRecentTrackById(trackId: Long)
    
    @Query("DELETE FROM recent_tracks WHERE playedAt < :timestamp")
    suspend fun deleteOlderThan(timestamp: Long)
    
    @Query("SELECT COUNT(*) FROM recent_tracks")
    suspend fun getCount(): Int
    
    @Query("DELETE FROM recent_tracks WHERE trackId IN (SELECT trackId FROM recent_tracks ORDER BY playedAt DESC LIMIT -1 OFFSET :keepCount)")
    suspend fun keepOnlyRecent(keepCount: Int = 100)
}

