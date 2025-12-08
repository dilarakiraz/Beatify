package com.dilara.beatify.di

import com.dilara.beatify.data.repository.FavoritesRepositoryImpl
import com.dilara.beatify.data.repository.MusicRepositoryImpl
import com.dilara.beatify.data.repository.PlaylistRepositoryImpl
import com.dilara.beatify.data.repository.RecentTracksRepositoryImpl
import com.dilara.beatify.data.repository.SearchHistoryRepositoryImpl
import com.dilara.beatify.domain.repository.FavoritesRepository
import com.dilara.beatify.domain.repository.MusicRepository
import com.dilara.beatify.domain.repository.PlaylistRepository
import com.dilara.beatify.domain.repository.RecentTracksRepository
import com.dilara.beatify.domain.repository.SearchHistoryRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    
    @Binds
    @Singleton
    abstract fun bindMusicRepository(
        musicRepositoryImpl: MusicRepositoryImpl
    ): MusicRepository
    
    @Binds
    @Singleton
    abstract fun bindFavoritesRepository(
        favoritesRepositoryImpl: FavoritesRepositoryImpl
    ): FavoritesRepository
    
    @Binds
    @Singleton
    abstract fun bindPlaylistRepository(
        playlistRepositoryImpl: PlaylistRepositoryImpl
    ): PlaylistRepository
    
    @Binds
    @Singleton
    abstract fun bindSearchHistoryRepository(
        searchHistoryRepositoryImpl: SearchHistoryRepositoryImpl
    ): SearchHistoryRepository
    
    @Binds
    @Singleton
    abstract fun bindRecentTracksRepository(
        recentTracksRepositoryImpl: RecentTracksRepositoryImpl
    ): RecentTracksRepository
}

