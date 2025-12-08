package com.dilara.beatify.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dilara.beatify.data.local.preferences.LanguagePreferences
import com.dilara.beatify.presentation.ui.components.profile.AppLanguage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class LanguageViewModel @Inject constructor(
    private val languagePreferences: LanguagePreferences
) : ViewModel() {
    
    val currentLanguage: StateFlow<AppLanguage> = languagePreferences.languageCode
        .map { code ->
            when (code) {
                "en" -> AppLanguage.EN
                else -> AppLanguage.TR
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = kotlinx.coroutines.flow.SharingStarted.WhileSubscribed(5000),
            initialValue = AppLanguage.TR
        )
    
    fun setLanguage(language: AppLanguage) {
        val languageCode = when (language) {
            AppLanguage.TR -> "tr"
            AppLanguage.EN -> "en"
        }
        languagePreferences.setLanguageCode(languageCode)
    }
}

