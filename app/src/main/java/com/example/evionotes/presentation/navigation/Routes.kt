package com.example.evionotes.presentation.navigation

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class Routes (
    val route: String,
    val arguments: List<NamedNavArgument> = emptyList()
) {
    object Splash : Routes("splash")

    object Login : Routes("login")

    object Register : Routes("register")

    object Home : Routes("home")

    object NoteDetail : Routes(
        route = "note_detail/{noteId}",
        arguments = listOf(
            navArgument("noteId") {
                type = NavType.StringType
                defaultValue = ""
            }
        )
    ) {
        fun createRoute(noteId: String = ""): String {
            return "note_detail/$noteId"
        }
    }

    object Settings : Routes("settings")
}