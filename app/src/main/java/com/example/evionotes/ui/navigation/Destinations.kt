package com.example.evionotes.ui.navigation

sealed class Destinations(val route: String) {
    object Home : Destinations("home")
    object NoteEditor : Destinations("note_editor")
    object NoteDetail : Destinations("note_detail")
    object Settings : Destinations("settings")
}