package com.dilara.beatify

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.dilara.beatify.presentation.ui.splash.SplashScreen
import com.dilara.beatify.ui.theme.BeatifyTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        // Install splash screen before super.onCreate()
        val splashScreen = installSplashScreen()
        
        super.onCreate(savedInstanceState)
        
        enableEdgeToEdge()
        
        var keepSplashOnScreen = true
        
        setContent {
            var showSplashContent by remember { mutableStateOf(true) }
            
            if (showSplashContent) {
                SplashScreen(
                    onSplashFinished = {
                        showSplashContent = false
                        keepSplashOnScreen = false
                    }
                )
            } else {
                BeatifyTheme {
                    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                        Greeting(
                            name = "Beatify",
                            modifier = Modifier.padding(innerPadding)
                        )
                    }
                }
            }
        }
        
        // Keep splash screen visible until content is ready
        splashScreen.setKeepOnScreenCondition { keepSplashOnScreen }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    BeatifyTheme {
        Greeting("Android")
    }
}