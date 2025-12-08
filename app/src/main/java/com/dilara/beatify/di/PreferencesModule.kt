package com.dilara.beatify.di

import android.content.Context
import com.dilara.beatify.data.local.preferences.LanguagePreferences
import com.dilara.beatify.data.local.preferences.ThemePreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PreferencesModule {
    
    @Provides
    @Singleton
    fun provideThemePreferences(
        @ApplicationContext context: Context
    ): ThemePreferences {
        return ThemePreferences(context)
    }
    
    @Provides
    @Singleton
    fun provideLanguagePreferences(
        @ApplicationContext context: Context
    ): LanguagePreferences {
        return LanguagePreferences(context)
    }
}



