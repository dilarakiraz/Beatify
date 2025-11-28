package com.dilara.beatify.di

import com.dilara.beatify.data.repository.FavoritesRepositoryImpl
import com.dilara.beatify.data.repository.MusicRepositoryImpl
import com.dilara.beatify.domain.repository.FavoritesRepository
import com.dilara.beatify.domain.repository.MusicRepository
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
}

