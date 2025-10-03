package com.example.evionotes.presentation.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryBlue,
    secondary = AccentBlue,
    tertiary = LightBlue,
    background = BackgroundDark,
    surface = SurfaceDark,
    onPrimary = PrimaryWhite,
    onSecondary = PrimaryWhite,
    onTertiary = DarkBlue,
    onBackground = OnSurfaceDark,
    onSurface = OnSurfaceDark,
    primaryContainer = SecondaryBlue,
    onPrimaryContainer = PrimaryWhite,
    secondaryContainer = DarkBlue,
    onSecondaryContainer = LightBlue
)

private val LightColorScheme = lightColorScheme(
    primary = PrimaryBlue,
    secondary = SecondaryBlue,
    tertiary = AccentBlue,
    background = BackgroundLight,
    surface = SurfaceLight,
    onPrimary = PrimaryWhite,
    onSecondary = PrimaryWhite,
    onTertiary = PrimaryWhite,
    onBackground = OnSurfaceLight,
    onSurface = OnSurfaceLight,
    primaryContainer = LightBlue,
    onPrimaryContainer = SecondaryBlue,
    secondaryContainer = SurfaceLight,
    onSecondaryContainer = SecondaryBlue
)

@Suppress("DEPRECATION")
@Composable
fun EvioNotesTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if(darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val view = LocalView.current
    if(!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}