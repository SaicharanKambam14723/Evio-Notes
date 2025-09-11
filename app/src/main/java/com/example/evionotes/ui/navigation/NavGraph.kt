package com.example.evionotes.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable


@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Routes.HOME) {
        composable(Routes.HOME) {

        }
        composable(Routes.NOTE_EDITOR) {

        }
        composable(Routes.NOTE_DETAIL) {

        }
        composable(Routes.SETTINGS) {

        }
    }
}