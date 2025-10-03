package com.example.evionotes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.evionotes.presentation.navigation.NotesNavigation
import com.example.evionotes.presentation.theme.EvioNotesTheme
import com.example.evionotes.presentation.viewmodel.SettingsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EvioNotesTheme {
                Surface(
                    color = MaterialTheme.colorScheme.background,
                    modifier = Modifier.fillMaxSize()
                ) {
                    val settingsViewModel: SettingsViewModel = hiltViewModel()
                    val isDarkMode by settingsViewModel.isDarkMode.collectAsState()

                    EvioNotesTheme(darkThemeFromSettings = isDarkMode) {
                        NotesNavigation()
                    }
                }
            }
        }
    }
}