package com.dilara.beatify.di

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.dilara.beatify.data.local.dao.FavoriteTrackDao
import com.dilara.beatify.data.local.dao.PlaylistDao
import com.dilara.beatify.data.local.database.BeatifyDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    
    private val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            try {
                database.execSQL("ALTER TABLE favorite_tracks ADD COLUMN position INTEGER NOT NULL DEFAULT 0")
            } catch (e: Exception) {
            }
        }
    }
    
    @Provides
    @Singleton
    fun provideBeatifyDatabase(
        @ApplicationContext context: Context
    ): BeatifyDatabase {
        return Room.databaseBuilder(
            context,
            BeatifyDatabase::class.java,
            "beatify_database"
        )
            .addMigrations(MIGRATION_1_2)
            .build()
    }
    
    @Provides
    @Singleton
    fun provideFavoriteTrackDao(database: BeatifyDatabase): FavoriteTrackDao {
        return database.favoriteTrackDao()
    }
    
    @Provides
    @Singleton
    fun providePlaylistDao(database: BeatifyDatabase): PlaylistDao {
        return database.playlistDao()
    }
}

