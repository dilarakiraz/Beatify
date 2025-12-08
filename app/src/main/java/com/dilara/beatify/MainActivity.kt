package com.dilara.beatify

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dilara.beatify.presentation.ui.components.profile.AppLanguage
import com.dilara.beatify.presentation.ui.navigation.BeatifyNavigation
import com.dilara.beatify.presentation.viewmodel.LanguageViewModel
import com.dilara.beatify.presentation.viewmodel.ThemeViewModel
import com.dilara.beatify.ui.theme.BeatifyTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        setContent {
            val themeViewModel: ThemeViewModel = hiltViewModel()
            val languageViewModel: LanguageViewModel = hiltViewModel()
            val isDarkTheme by themeViewModel.isDarkTheme.collectAsStateWithLifecycle()
            val currentLanguage by languageViewModel.currentLanguage.collectAsStateWithLifecycle()
            
            // Update locale when language changes
            UpdateLocaleEffect(currentLanguage)
            
            BeatifyTheme(darkTheme = isDarkTheme) {
                BeatifyNavigation()
            }
        }
    }
}

@Composable
private fun UpdateLocaleEffect(language: AppLanguage) {
    val context = LocalContext.current
    val activity = context as? ComponentActivity
    var previousLanguage by remember { mutableStateOf<AppLanguage?>(null) }
    
    LaunchedEffect(language) {
        // Only recreate if language actually changed
        if (previousLanguage != null && previousLanguage != language) {
            // Recreate activity to apply locale change
            // Use post to ensure UI updates are complete
            activity?.window?.decorView?.post {
                activity.recreate()
            }
        }
        previousLanguage = language
    }
}