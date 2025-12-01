package com.dilara.beatify.di

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.dilara.beatify.data.local.dao.FavoriteTrackDao
import com.dilara.beatify.data.local.dao.PlaylistDao
import com.dilara.beatify.data.local.dao.SearchHistoryDao
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
    
    private val MIGRATION_2_3 = object : Migration(2, 3) {
        override fun migrate(database: SupportSQLiteDatabase) {
            try {
                database.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS search_history (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        query TEXT NOT NULL,
                        searchedAt INTEGER NOT NULL
                    )
                    """.trimIndent()
                )
            } catch (_: Exception) {
            }
        }
    }
    
    private val MIGRATION_3_4 = object : Migration(3, 4) {
        override fun migrate(database: SupportSQLiteDatabase) {
            try {
                database.execSQL("DROP TABLE IF EXISTS search_history")
                database.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS search_history (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        trackId INTEGER NOT NULL,
                        title TEXT NOT NULL,
                        titleShort TEXT NOT NULL,
                        duration INTEGER NOT NULL,
                        previewUrl TEXT,
                        artistId INTEGER NOT NULL,
                        artistName TEXT NOT NULL,
                        albumId INTEGER NOT NULL,
                        albumTitle TEXT NOT NULL,
                        albumCover TEXT,
                        searchedAt INTEGER NOT NULL
                    )
                    """.trimIndent()
                )
            } catch (_: Exception) {
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
            .addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4)
            .fallbackToDestructiveMigration()
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
    
    @Provides
    @Singleton
    fun provideSearchHistoryDao(database: BeatifyDatabase): SearchHistoryDao {
        return database.searchHistoryDao()
    }
}

