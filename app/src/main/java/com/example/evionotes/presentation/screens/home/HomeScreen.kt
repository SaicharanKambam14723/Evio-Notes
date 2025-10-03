package com.example.evionotes.presentation.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.evionotes.presentation.viewmodel.NotesViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.example.evionotes.presentation.components.NoteCard
import com.example.evionotes.presentation.components.SearchBar
import com.example.evionotes.presentation.theme.PrimaryBlue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToNoteDetail: (String) -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToLogin: () -> Unit,
    viewModel: NotesViewModel = hiltViewModel()
) {
    val currentUser by viewModel.currentUser.collectAsState()
    val notes = viewModel.notes.collectAsLazyPagingItems()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val searchResults by viewModel.searchResults.collectAsState()

    LaunchedEffect(currentUser) {
        if(currentUser == null) {
            onNavigateToLogin()
        }
    }

    var isSearchActive by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.run {
                    verticalGradient(
                        colors = listOf(
                            PrimaryBlue.copy(alpha = 0.05f),
                            MaterialTheme.colorScheme.background
                        )
                    )
                }
            )
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = if(isSearchActive && searchQuery.isNotBlank()) "Search Results" else "My Notes",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )
                        currentUser?.let { user ->
                            Text(
                                text = "Welcome, ${user.username}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            isSearchActive = !isSearchActive
                        }
                    ) {
                       Icon(
                           imageVector = Icons.Default.Search,
                           contentDescription = "Search"
                       )
                    }
                    IconButton(
                        onClick = {
                            onNavigateToSettings()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Settings"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f)
                )
            )

            if(isSearchActive) {
                SearchBar(
                    query = searchQuery,
                    onQueryChange = {
                        viewModel.searchNotes(it)
                    },
                    onSearch = {

                    },
                    onActiveChange = {
                        if(!it) {
                            viewModel.clearSearch()
                        }
                    },
                    modifier = Modifier.padding(16.dp)
                )
            }

            val displayNotes = if(searchQuery.isNotBlank()) searchResults else null

            if(displayNotes?.isEmpty() == true && searchQuery.isNotBlank()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "\uD83D\uDD0D",
                            style = MaterialTheme.typography.displayMedium
                        )
                        Spacer(
                            modifier = Modifier.height(16.dp)
                        )
                        Text(
                            text = "No Notes found",
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "Try searching for something else",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else if(notes.itemCount == 0 && searchQuery.isBlank()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "\uD83D\uDCDD",
                            style = MaterialTheme.typography.displayLarge
                        )
                    }
                }

                Spacer(
                    modifier = Modifier.height(24.dp)
                )

                Text(
                    text = "Create your first note by tapping the '+' button",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 8.dp)
                )
            } else {
                LazyVerticalStaggeredGrid(
                    columns = StaggeredGridCells.Adaptive(minSize = 160.dp),
                    contentPadding = PaddingValues(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalItemSpacing = 12.dp,
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    if (searchQuery.isNotBlank() && displayNotes != null) {
                        items(displayNotes.size) { index ->
                            NoteCard(
                                note = displayNotes[index],
                                onClicks = {
                                    onNavigateToNoteDetail(displayNotes[index].id)
                                },
                                onPin = {
                                    viewModel.togglePinNote(displayNotes[index].id)
                                },
                                onArchive = {
                                    viewModel.toggleArchiveNote(displayNotes[index].id)
                                },
                                onDelete = {
                                    viewModel.deleteNote(displayNotes[index].id)
                                }
                            )
                        }
                    } else {
                        items(
                            count = notes.itemCount,
                            key = notes.itemKey {
                                it.id
                            }
                        ) { index ->
                            notes[index]?.let { note ->
                                NoteCard(
                                    note = note,
                                    onClicks = {
                                        onNavigateToNoteDetail(note.id)
                                    },
                                    onPin = {
                                        viewModel.togglePinNote(note.id)
                                    },
                                    onArchive = {
                                        viewModel.toggleArchiveNote(note.id)
                                    },
                                    onDelete = {
                                        viewModel.deleteNote(note.id)
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }

        FloatingActionButton(
            onClick = {
                onNavigateToNoteDetail("")
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            containerColor = PrimaryBlue,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Create Note",
                modifier = Modifier.size(24.dp)
            )
        }
    }
}