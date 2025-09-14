package com.example.evionotes.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable


@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = NavRoutes.HOME) {
        composable(NavRoutes.HOME) {

        }
        composable(NavRoutes.NOTE_EDITOR) {

        }
        composable(NavRoutes.NOTE_DETAIL) {

        }
        composable(NavRoutes.SETTINGS) {

        }
    }
}