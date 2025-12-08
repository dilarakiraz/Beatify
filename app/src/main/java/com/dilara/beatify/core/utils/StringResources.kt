package com.dilara.beatify.core.utils

import android.content.Context
import android.content.res.Configuration
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import java.util.Locale

@Composable
@ReadOnlyComposable
fun stringResource(@StringRes id: Int): String {
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val config = Configuration(configuration)
    val locale = getCurrentLocale(context)
    config.setLocale(locale)
    val localizedContext = context.createConfigurationContext(config)
    return localizedContext.getString(id)
}

@Composable
@ReadOnlyComposable
fun stringResource(@StringRes id: Int, vararg formatArgs: Any): String {
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val config = Configuration(configuration)
    val locale = getCurrentLocale(context)
    config.setLocale(locale)
    val localizedContext = context.createConfigurationContext(config)
    return localizedContext.getString(id, *formatArgs)
}

private fun getCurrentLocale(context: Context): Locale {
    val prefs = context.getSharedPreferences("language_prefs", Context.MODE_PRIVATE)
    val languageCode = prefs.getString("language_code", "tr") ?: "tr"
    return Locale(languageCode)
}

