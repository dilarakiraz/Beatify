package com.dilara.beatify.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.dilara.beatify.data.local.dao.FavoriteTrackDao
import com.dilara.beatify.data.local.dao.PlaylistDao
import com.dilara.beatify.data.local.entity.FavoriteTrackEntity
import com.dilara.beatify.data.local.entity.PlaylistEntity
import com.dilara.beatify.data.local.entity.PlaylistTrackEntity

@Database(
    entities = [
        FavoriteTrackEntity::class,
        PlaylistEntity::class,
        PlaylistTrackEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class BeatifyDatabase : RoomDatabase() {
    abstract fun favoriteTrackDao(): FavoriteTrackDao
    abstract fun playlistDao(): PlaylistDao
}

