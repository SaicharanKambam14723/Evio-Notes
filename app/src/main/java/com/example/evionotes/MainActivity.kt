package com.example.evionotes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.example.evionotes.ui.navigation.AppNavHost
import com.example.evionotes.ui.theme.EvioNotesTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val isDarkTheme = remember { mutableStateOf(false) }
            AppNavHost(
                isDarkTheme = isDarkTheme.value,
                onToggleDarkTheme = { isDarkTheme.value = it }
            )

        }
    }
}