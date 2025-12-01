package com.dilara.beatify.data.repository

import com.dilara.beatify.core.utils.safeApiCall
import com.dilara.beatify.data.local.dao.SearchHistoryDao
import com.dilara.beatify.data.mapper.toDomain
import com.dilara.beatify.data.mapper.toSearchHistoryEntity
import com.dilara.beatify.domain.repository.SearchHistoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SearchHistoryRepositoryImpl @Inject constructor(
    private val searchHistoryDao: SearchHistoryDao
) : SearchHistoryRepository {
    
    override fun getRecentSearches(limit: Int): Flow<List<com.dilara.beatify.domain.model.SearchHistory>> {
        return searchHistoryDao.getRecentSearches(limit).map { entities ->
            entities.map { it.toDomain() }
        }
    }
    
    override suspend fun addSearch(track: com.dilara.beatify.domain.model.Track): Result<Unit> {
        return safeApiCall {
            val existing = searchHistoryDao.getLatestSearchByTrackId(track.id)
            if (existing != null) {
                searchHistoryDao.insertSearch(
                    existing.copy(searchedAt = System.currentTimeMillis())
                )
            } else {
                searchHistoryDao.insertSearch(track.toSearchHistoryEntity())
            }
        }
    }
    
    override suspend fun deleteSearch(id: Long): Result<Unit> {
        return safeApiCall {
            searchHistoryDao.deleteSearch(id)
        }
    }
    
    override suspend fun clearAll(): Result<Unit> {
        return safeApiCall {
            searchHistoryDao.clearAll()
        }
    }
    
    override suspend fun hasSearchHistory(): Boolean {
        return searchHistoryDao.getSearchCount() > 0
    }
}

