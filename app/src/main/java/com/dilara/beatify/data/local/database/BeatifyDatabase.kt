package com.dilara.beatify.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.dilara.beatify.data.local.dao.FavoriteTrackDao
import com.dilara.beatify.data.local.dao.PlaylistDao
import com.dilara.beatify.data.local.dao.SearchHistoryDao
import com.dilara.beatify.data.local.entity.FavoriteTrackEntity
import com.dilara.beatify.data.local.entity.PlaylistEntity
import com.dilara.beatify.data.local.entity.PlaylistTrackEntity
import com.dilara.beatify.data.local.entity.SearchHistoryEntity

@Database(
    entities = [
        FavoriteTrackEntity::class,
        PlaylistEntity::class,
        PlaylistTrackEntity::class,
        SearchHistoryEntity::class
    ],
    version = 4,
    exportSchema = false
)
abstract class BeatifyDatabase : RoomDatabase() {
    abstract fun favoriteTrackDao(): FavoriteTrackDao
    abstract fun playlistDao(): PlaylistDao
    abstract fun searchHistoryDao(): SearchHistoryDao
}

