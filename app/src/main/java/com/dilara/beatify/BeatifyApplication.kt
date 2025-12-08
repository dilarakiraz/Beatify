package com.dilara.beatify

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import com.dilara.beatify.core.utils.LocaleHelper
import dagger.hilt.android.HiltAndroidApp
import java.util.Locale

private const val PREFS_NAME = "language_prefs"
private const val KEY_LANGUAGE_CODE = "language_code"

@HiltAndroidApp
class BeatifyApplication : Application() {
    companion object {
        private var currentLocale: Locale? = null
    }
    
    override fun onCreate() {
        super.onCreate()
        updateLocale()
    }
    
    override fun attachBaseContext(base: Context?) {
        val languageCode = base?.let {
            try {
                val prefs: SharedPreferences = it.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                prefs.getString(KEY_LANGUAGE_CODE, "tr") ?: "tr"
            } catch (e: Exception) {
                "tr"
            }
        } ?: "tr"
        
        val locale = Locale(languageCode)
        currentLocale = locale
        Locale.setDefault(locale)
        
        val context = base?.let { LocaleHelper.setLocale(it, languageCode) } ?: base
        super.attachBaseContext(context)
    }
    
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        currentLocale?.let {
            newConfig.setLocale(it)
        }
    }
    
    private fun updateLocale() {
        val languageCode = try {
            val prefs: SharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            prefs.getString(KEY_LANGUAGE_CODE, "tr") ?: "tr"
        } catch (e: Exception) {
            "tr"
        }
        
        val locale = Locale(languageCode)
        currentLocale = locale
        Locale.setDefault(locale)
        
        val config = Configuration(resources.configuration)
        config.setLocale(locale)
        @Suppress("DEPRECATION")
        resources.updateConfiguration(config, resources.displayMetrics)
    }
}




