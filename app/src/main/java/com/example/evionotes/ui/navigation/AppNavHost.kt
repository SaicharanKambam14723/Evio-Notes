package com.example.evionotes.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.evionotes.ui.notes.list.ImageGalleryScreen
import com.example.evionotes.ui.notes.list.NoteDetailScreen
import com.example.evionotes.ui.notes.list.NotesHomeScreen
import com.example.evionotes.ui.notes.list.NotesViewModel
import com.example.evionotes.ui.notes.list.UiEvent
import com.example.evionotes.ui.settings.SettingsScreen
import com.example.evionotes.ui.splash.SplashScreen

@Composable
fun AppNavHost(
    isDarkTheme: Boolean,
    onToggleDarkTheme: (Boolean) -> Unit,
    navController: NavHostController = rememberNavController(),
    notesViewModel: NotesViewModel = hiltViewModel()
) {
    val events = notesViewModel.events.collectAsState(initial = null)

    LaunchedEffect(events.value) {
        when(val event = events.value) {
            is UiEvent.NavigateToNote -> navController.navigate("${NavRoutes.NOTE_DETAIL}/${event.noteId}")
            UiEvent.NavigateToAddNote -> navController.navigate(NavRoutes.NOTE_DETAIL)
            is UiEvent.ShowImageGallery -> {
                val imagesArg = event.images.joinToString(separator = ",")
                navController.navigate("${NavRoutes.IMAGE_GALLERY}/$imagesArg")
            }
            null -> {}
        }
    }

    NavHost(navController = navController, startDestination = NavRoutes.HOME) {
        composable(NavRoutes.Splash) {
            SplashScreen(
                onTimeout = {
                    navController.navigate(NavRoutes.HOME) {
                        popUpTo(NavRoutes.Splash) {
                            inclusive = true
                        }
                    }
                }
            )
        }
        composable(NavRoutes.HOME) {
            NotesHomeScreen(
                notesList = notesViewModel.notes.collectAsState().value,
                onNoteClick = {
                    notesViewModel.onNoteSelected(it)
                },
                onViewImages = {
                    notesViewModel.onViewImages(it)
                },
                onAddNote = {
                    notesViewModel.onAddNoteClicked()
                },
                onSettingsClick = {
                    navController.navigate(NavRoutes.SETTINGS)
                },
                onSearch = {
                    notesViewModel.onSearchQueryChanged(it)
                }
            )
        }
        composable(
            "${NavRoutes.NOTE_DETAIL}/{noteId}",
            arguments = listOf(navArgument("noteId") {
                type = NavType.IntType
            } )
        ) { backStackEntry ->
            val noteId = backStackEntry.arguments?.getInt("noteId") ?: 0
            NoteDetailScreen(
                noteId = noteId,
                onBackClick = {navController.popBackStack()}
            )
        }
        composable(NavRoutes.NOTE_DETAIL) {
            NoteDetailScreen(
                noteId = -1,
                onBackClick = {navController.popBackStack()}
            )
        }
        composable(
            route = "${NavRoutes.IMAGE_GALLERY}/{imageIds}",
            arguments = listOf(navArgument("imageIds") {
                type = NavType.StringType
            })
        ) { backStackEntry ->
            val imageIdsArg = backStackEntry.arguments?.getString("images")
            val imageIds = imageIdsArg?.split(",")?.map { it.toInt() } ?: emptyList()
            ImageGalleryScreen(
                imageIds = imageIds,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(NavRoutes.SETTINGS) {
            SettingsScreen(
                isDarkTheme = isDarkTheme,
                onToggleDarkTheme = {
                    onToggleDarkTheme
                }
            )
        }
    }
}

