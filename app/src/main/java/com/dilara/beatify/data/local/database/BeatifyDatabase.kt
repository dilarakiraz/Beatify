package com.dilara.beatify.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.dilara.beatify.data.local.dao.FavoriteTrackDao
import com.dilara.beatify.data.local.entity.FavoriteTrackEntity

@Database(
    entities = [FavoriteTrackEntity::class],
    version = 1,
    exportSchema = false
)
abstract class BeatifyDatabase : RoomDatabase() {
    abstract fun favoriteTrackDao(): FavoriteTrackDao
}

