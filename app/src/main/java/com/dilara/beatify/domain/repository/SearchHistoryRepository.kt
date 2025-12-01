package com.dilara.beatify.domain.repository

import com.dilara.beatify.domain.model.SearchHistory
import com.dilara.beatify.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface SearchHistoryRepository {
    fun getRecentSearches(limit: Int = 10): Flow<List<SearchHistory>>
    suspend fun addSearch(track: Track): Result<Unit>
    suspend fun deleteSearch(id: Long): Result<Unit>
    suspend fun clearAll(): Result<Unit>
    suspend fun hasSearchHistory(): Boolean
}

