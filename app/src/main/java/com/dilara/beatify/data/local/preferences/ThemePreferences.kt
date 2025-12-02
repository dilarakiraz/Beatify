package com.dilara.beatify.data.local.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.themeDataStore: DataStore<Preferences> by preferencesDataStore(name = "theme_preferences")

private object ThemePreferencesKeys {
    val IS_DARK_THEME = booleanPreferencesKey("is_dark_theme")
}

@Singleton
class ThemePreferences @Inject constructor(
    private val context: Context
) {
    val isDarkTheme: Flow<Boolean> = context.themeDataStore.data
        .map { preferences ->
            preferences[ThemePreferencesKeys.IS_DARK_THEME] ?: true // Default to dark theme
        }

    suspend fun setDarkTheme(isDark: Boolean) {
        context.themeDataStore.edit { preferences ->
            preferences[ThemePreferencesKeys.IS_DARK_THEME] = isDark
        }
    }
}

