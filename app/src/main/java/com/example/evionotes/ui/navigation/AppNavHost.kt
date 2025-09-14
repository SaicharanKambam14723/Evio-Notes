package com.example.evionotes.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.evionotes.ui.notes.list.NotesHomeScreen
import com.example.evionotes.ui.notes.list.NotesViewModel
import com.example.evionotes.ui.notes.list.UiEvent
import com.example.evionotes.ui.splash.SplashScreen

@Composable
fun AppNavHost(
    navController: NavHostController = rememberNavController(),
    notesViewModel: NotesViewModel = hiltViewModel()
) {
    val events = notesViewModel.events.collectAsState(initial = null)

    LaunchedEffect(events.value) {
        when(val event = events.value) {
            is UiEvent.NavigateToNote -> navController.navigate("${NavRoutes.NOTE_DETAIL}/${event.noteId}")
            UiEvent.NavigateToAddNote -> navController.navigate(NavRoutes.AddNote)
            is UiEvent.ShowImageGallery -> {
                val imagesArg = event.images.joinToString(separator = ",")
                navController.navigate("${NavRoutes.IMAGE_GALLERY}/$imagesArg")
            }
            null -> Unit
        }
    }

    NavHost(navController = navController, startDestination = NavRoutes.Splash) {
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
                    notesViewModel.
                }
            )
        }
    }
}