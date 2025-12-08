package com.dilara.beatify.data.repository

import com.dilara.beatify.core.utils.safeApiCall
import com.dilara.beatify.data.local.dao.RecentTrackDao
import com.dilara.beatify.data.mapper.toDomain
import com.dilara.beatify.data.mapper.toRecentEntity
import com.dilara.beatify.domain.model.Track
import com.dilara.beatify.domain.repository.RecentTracksRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecentTracksRepositoryImpl @Inject constructor(
    private val recentTrackDao: RecentTrackDao
) : RecentTracksRepository {
    
    override fun getRecentTracks(limit: Int): Flow<List<Track>> {
        return recentTrackDao.getRecentTracks(limit).map { entities ->
            entities.mapNotNull { entity ->
                try {
                    entity.toDomain()
                } catch (e: Exception) {
                    e.printStackTrace()
                    null
                }
            }
        }
    }
    
    override suspend fun addRecentTrack(track: Track): Result<Unit> {
        return safeApiCall {
            recentTrackDao.insertRecentTrack(track.toRecentEntity())
            
            recentTrackDao.keepOnlyRecent(keepCount = 100)
        }
    }
    
    override suspend fun clearRecentTracks(): Result<Unit> {
        return safeApiCall {
            val allTracks = recentTrackDao.getRecentTracksSync(Int.MAX_VALUE)
            allTracks.forEach { track ->
                recentTrackDao.deleteRecentTrackById(track.trackId)
            }
        }
    }
}

