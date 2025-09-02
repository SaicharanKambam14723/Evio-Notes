package com.example.evionotes.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import com.evio.notes.ui.theme.DarkBackground
import com.evio.notes.ui.theme.DarkOnBackground
import com.evio.notes.ui.theme.DarkOnPrimary
import com.evio.notes.ui.theme.DarkOnSurface
import com.evio.notes.ui.theme.DarkPrimary
import com.evio.notes.ui.theme.DarkSurface
import com.evio.notes.ui.theme.LightBackground
import com.evio.notes.ui.theme.LightOnBackground
import com.evio.notes.ui.theme.LightOnPrimary
import com.evio.notes.ui.theme.LightOnSurface
import com.evio.notes.ui.theme.LightPrimary
import com.evio.notes.ui.theme.LightSurface
import com.evio.notes.ui.theme.RedError

private val LightColors = lightColorScheme(
    primary = LightPrimary,
    onPrimary = LightOnPrimary,
    background = LightBackground,
    onBackground = LightOnBackground,
    surface = LightSurface,
    onSurface = LightOnSurface,
    error = RedError
)

private val DarkColors = darkColorScheme(
    primary = DarkPrimary,
    onPrimary = DarkOnPrimary,
    background = DarkBackground,
    onBackground = DarkOnBackground,
    surface = DarkSurface,
    onSurface = DarkOnSurface,
    error = RedError
)

@Composable
fun EvioNotesTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColors else LightColors

    MaterialTheme(
        colorScheme = colors,
        typography = AppTypography,
        shapes = AppShapes,
        content = content
    )
}
