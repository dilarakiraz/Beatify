package com.dilara.beatify.data.local.preferences

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton

private const val PREFS_NAME = "language_prefs"
private const val KEY_LANGUAGE_CODE = "language_code"

@Singleton
class LanguagePreferences @Inject constructor(
    private val context: Context
) {
    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    private val _languageCode = MutableStateFlow(prefs.getString(KEY_LANGUAGE_CODE, "tr") ?: "tr")
    
    val languageCode: Flow<String> = _languageCode.asStateFlow()

    fun setLanguageCode(languageCode: String) {
        prefs.edit().putString(KEY_LANGUAGE_CODE, languageCode).apply()
        _languageCode.update { languageCode }
    }
}

