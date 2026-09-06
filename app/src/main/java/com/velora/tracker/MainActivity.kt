package com.velora.tracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.velora.tracker.navigation.VeloraNavHost
import com.velora.tracker.ui.theme.VeloraTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val app = application as VeloraApplication
            val settingsRepository = app.settingsRepository
            val themeMode by settingsRepository.getThemeMode().collectAsState(initial = 0)
            
            val darkTheme = when (themeMode) {
                1 -> false // Light
                2 -> true  // Dark
                else -> isSystemInDarkTheme()
            }
            
            VeloraTheme(darkTheme = darkTheme, dynamicColor = true) {
                VeloraNavHost(application = app)
            }
        }
    }
}
