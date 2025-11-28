package com.dilara.beatify

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.dilara.beatify.presentation.ui.navigation.BeatifyNavigation
import com.dilara.beatify.ui.theme.BeatifyTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        // Install splash screen before super.onCreate()
        val splashScreen = installSplashScreen()
        
        super.onCreate(savedInstanceState)
        
        enableEdgeToEdge()
        
        setContent {
            BeatifyTheme {
                BeatifyNavigation()
            }
        }
        
        // Keep splash screen visible until navigation is ready
        splashScreen.setKeepOnScreenCondition { false }
    }
}