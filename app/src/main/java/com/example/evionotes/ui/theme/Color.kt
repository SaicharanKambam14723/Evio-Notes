package com.evio.notes.ui.theme

import androidx.compose.ui.graphics.Color

// Brand Colors
val EvioBluePrimary = Color(0xFF5CB8EC)
val EvioBlueSecondary = Color(0xFF5CB9EC)
val EvioNavy = Color(0xFF193872)
val EvioWhite = Color(0xFFFFFFFF)

// Neutral Shades
val EvioBlack = Color(0xFF000000)
val EvioDarkGray = Color(0xFF1C1C1E)
val EvioGray = Color(0xFF8E8E93)
val EvioLightGray = Color(0xFFD1D1D6)
val EvioExtraLightGray = Color(0xFFF2F2F7)

// Semantic Colors
val SuccessGreen = Color(0xFF4CAF50)
val WarningOrange = Color(0xFFFFA726)
val ErrorRed = Color(0xFFE53935)
val InfoBlue = Color(0xFF42A5F5)

// Markdown / Editor Colors
val MarkdownHeading = EvioNavy
val MarkdownText = EvioBlack
val MarkdownLink = EvioBluePrimary
val MarkdownCodeBg = EvioLightGray
val MarkdownBlockquote = EvioGray

// Light Theme Palette
object LightColors {
    val Primary = EvioBluePrimary
    val OnPrimary = EvioWhite
    val Background = EvioWhite
    val OnBackground = EvioBlack
    val Surface = EvioExtraLightGray
    val OnSurface = EvioBlack
    val Outline = EvioLightGray
}

// Dark Theme Palette
object DarkColors {
    val Primary = EvioBlueSecondary
    val OnPrimary = EvioBlack
    val Background = EvioDarkGray
    val OnBackground = EvioWhite
    val Surface = EvioBlack
    val OnSurface = EvioWhite
    val Outline = EvioGray
}