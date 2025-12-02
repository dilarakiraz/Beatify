package com.dilara.beatify.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dilara.beatify.data.local.preferences.ThemePreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ThemeViewModel @Inject constructor(
    private val themePreferences: ThemePreferences
) : ViewModel() {
    
    val isDarkTheme: StateFlow<Boolean> = themePreferences.isDarkTheme
        .stateIn(
            scope = viewModelScope,
            started = kotlinx.coroutines.flow.SharingStarted.WhileSubscribed(5000),
            initialValue = true
        )
    
    fun toggleTheme() {
        viewModelScope.launch {
            themePreferences.setDarkTheme(!isDarkTheme.value)
        }
    }
    
    fun setDarkTheme(isDark: Boolean) {
        viewModelScope.launch {
            themePreferences.setDarkTheme(isDark)
        }
    }
}

