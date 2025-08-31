package com.example.evionotes.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.evio.notes.ui.theme.DarkColors
import com.evio.notes.ui.theme.LightColors
import com.example.evionotes.R

val InterFontFamily = FontFamily(
    Font(R.font.inter_28pt_regular, FontWeight.Normal),
    Font(R.font.inter_28pt_medium, FontWeight.Medium),
    Font(R.font.inter_28pt_semibold, FontWeight.SemiBold),
    Font(R.font.inter_28pt_bold, FontWeight.Bold)
)

private val LightColorScheme = lightColorScheme(
    primary = LightColors.Primary,
    onPrimary = LightColors.OnPrimary,
    background = LightColors.Background,
    onBackground = LightColors.OnBackground,
    surface = LightColors.Surface,
    onSurface = LightColors.OnSurface,
    outline = LightColors.Outline
)

private val DarkColorScheme = darkColorScheme(
    primary = DarkColors.Primary,
    onPrimary = DarkColors.OnPrimary,
    background = DarkColors.Background,
    onBackground = DarkColors.OnBackground,
    surface = DarkColors.Surface,
    onSurface = DarkColors.OnSurface,
    outline = DarkColors.Outline
)



@Composable
fun EvioNotesTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = if(darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = EvioTypography,
        shapes = EvioShapes,
        content = content
    )
}