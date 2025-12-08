package com.dilara.beatify.core.utils

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import java.util.Locale

object LocaleHelper {
    fun setLocale(context: Context, languageCode: String): Context {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        
        val resources: Resources = context.resources
        val configuration: Configuration = resources.configuration
        configuration.setLocale(locale)
        configuration.setLayoutDirection(locale)
        
        return context.createConfigurationContext(configuration)
    }
}

