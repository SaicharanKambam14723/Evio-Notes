package com.example.evionotes.presentation.screens.note_detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Label
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.outlined.PushPin
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.evionotes.presentation.components.ColorPicker
import com.example.evionotes.presentation.components.MarkdownRenderer
import com.example.evionotes.presentation.theme.PrimaryBlue
import com.example.evionotes.presentation.viewmodel.NoteDetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteDetailScreen(
    noteId: String,
    onNavigateBack: () -> Unit,
    viewModel: NoteDetailViewModel = hiltViewModel()
) {
    val note by viewModel.note.collectAsState()

    val isLoading by viewModel.isLoading.collectAsState()

    val isSaved by viewModel.isSaved.collectAsState()

    val error by viewModel.error.collectAsState()

    var isPreviewMode by remember { mutableStateOf(false) }
    var showColorPicker by remember { mutableStateOf(false) }
    var showTagDialog by remember { mutableStateOf(false) }

    LaunchedEffect(noteId) {
        if(noteId.isNotEmpty()) {
            viewModel.loadNote(noteId)
        }
    }

    LaunchedEffect(isSaved) {
        if(isSaved) {
            viewModel.resetSaveStatus()
            onNavigateBack()
        }
    }

    val backgroundColor = note?.let { Color(it.color) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                (if(backgroundColor == Color.White) {
                    MaterialTheme.colorScheme.background
                } else {
                    backgroundColor?.copy(alpha = 0.05f)
                })!!
            )
    ) {
        TopAppBar(
            title = {
                Text(
                    text = ""
                )
            },
            navigationIcon = {
                IconButton(
                    onClick = onNavigateBack
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            },
            actions = {
                IconButton(
                    onClick = {
                        viewModel.togglePin()
                    }
                ) {
                    Icon(
                        imageVector = if(note?.isPinned == true) Icons.Default.PushPin else Icons.Outlined.PushPin,
                        contentDescription = if(note?.isPinned == true) "Unpin" else "Pin",
                        tint = if(note?.isPinned == true) PrimaryBlue else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                IconButton(
                    onClick = { isPreviewMode = !isPreviewMode }
                ) {
                    Icon(
                        imageVector = if(isPreviewMode) Icons.Default.Edit else Icons.Default.Visibility,
                        contentDescription = if(isPreviewMode) "Edit" else "Preview"
                    )
                }

                var showMenu by remember { mutableStateOf(false) }

                IconButton(
                    onClick = {showMenu = true}
                ) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "More"
                    )
                }

                DropdownMenu(
                    expanded = showMenu,
                    onDismissRequest = { showMenu = false }
                ) {
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = "Change Color"
                            )
                        },
                        onClick = {
                            showColorPicker = true
                            showMenu = false
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Palette,
                                contentDescription = "Change Color"
                            )
                        }
                    )

                    DropdownMenuItem(
                        text = {
                            Text(
                                text = "Manage Tags"
                            )
                        },
                        onClick = {
                            showTagDialog = true
                            showMenu = false
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Tag,
                                contentDescription = "Manage Tags"
                            )
                        }
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f)
            )
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            if(isPreviewMode) {
                if(note?.title?.isNotBlank() == true) {
                    Text(
                        text = note!!.title,
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                }
                if(note?.content?.isNotBlank() == true) {
                    MarkdownRenderer(
                        content = note!!.content,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            } else {
                OutlinedTextField(
                    value = note?.title.toString(),
                    onValueChange = { newTitle ->
                        viewModel.updateTitle(newTitle)
                    },
                    placeholder = {
                        Text(
                            text = "Title"
                        )
                    },
                    textStyle = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                )

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                OutlinedTextField(
                    value = note?.content.toString(),
                    onValueChange = { newContent ->
                        viewModel.updateContent(newContent)
                    },
                    placeholder = {
                        Text(
                            text = "Write your note here..."
                        )
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .defaultMinSize(minHeight = 300.dp)
                )
            }
            if(note?.imageUrls?.isNotEmpty() == true) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Images",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(note!!.imageUrls.size) { imgIndex ->
                        Box {
                            AsyncImage(
                                model = note!!.imageUrls[imgIndex],
                                contentDescription = "Image",
                                modifier = Modifier
                                    .size(120.dp)
                                    .clip(RoundedCornerShape(12.dp)),
                                contentScale = ContentScale.Crop
                            )

                            if(!isPreviewMode) {
                                IconButton(
                                    onClick = {
                                        viewModel.removeImage(note!!.imageUrls[imgIndex])
                                    },
                                    modifier = Modifier.align(Alignment.TopEnd)
                                ) {
                                    Surface(
                                        shape = RoundedCornerShape(12.dp),
                                        color = MaterialTheme.colorScheme.error
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Close,
                                            contentDescription = "Remove Image",
                                            tint = MaterialTheme.colorScheme.onError,
                                            modifier = Modifier.padding(4.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            if(note?.tags?.isNotEmpty() == true) {
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Tags",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(note!!.tags.size) { tagIndex ->
                        AssistChip(
                            onClick = {  },
                            label = {
                                Text(
                                    text = note!!.tags[tagIndex]
                                )
                            },
                            trailingIcon = if(!isPreviewMode) {
                                {
                                    IconButton(
                                        onClick = {
                                            viewModel.removeTag(note!!.tags[tagIndex])
                                        },
                                        modifier = Modifier.size(16.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Close,
                                            contentDescription = "Remove Tag",
                                            modifier = Modifier.size(12.dp)
                                        )
                                    }
                                }
                            } else null
                        )
                    }
                }
            }
        }
        if(!isPreviewMode) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.surface,
                shadowElevation = 8.dp
            ) {
                Row(
                    modifier = Modifier
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    IconButton(
                        onClick = { }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Image,
                            contentDescription = "Add Image"
                        )
                    }

                    IconButton(
                        onClick = {
                            showTagDialog = true
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Label,
                            contentDescription = "Add tag"
                        )
                    }

                    IconButton(
                        onClick = {
                            showColorPicker = true
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Palette,
                            contentDescription = "Change color"
                        )
                    }

                    IconButton(
                        onClick = {  }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Checklist,
                            contentDescription = "Add checklist"
                        )
                    }
                    Button(
                        onClick = {
                            viewModel.saveNote()
                        },
                        enabled = !isLoading,
                    ) {
                        if(isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        } else {
                            Text(
                                text = "Save"
                            )
                        }
                    }
                }
            }
        }
    }

    if(showColorPicker) {
        AlertDialog(
            onDismissRequest = {
                showColorPicker = false
            },
            title = {
                Text(
                    text = "Choose Color"
                )
            },
            text = {
                ColorPicker(
                    selectedColor = Color(color = note!!.color),
                    onColorSelected = { colord ->
                        viewModel.updateColor(colord)
                        showColorPicker = false
                    }
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showColorPicker = false
                    }
                ) {
                    Text(
                        text = "Cancel"
                    )
                }
            }
        )
    }

    if(showTagDialog) {
        var newTag by remember { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = {
                showTagDialog = false
            },
            title = {
                Text(
                    text = "Add Tag"
                )
            },
            text = {
                OutlinedTextField(
                    value = newTag,
                    onValueChange = { newTag = it },
                    label = {
                        Text(
                            text = "Tag name"
                        )
                    },
                    singleLine = true
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if(newTag.isNotBlank()) {
                            viewModel.addTag(newTag)
                            newTag = ""
                            showTagDialog = false
                        }
                    }
                ) {
                    Text(
                        text = "Add"
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showTagDialog = false
                    }
                ) {
                    Text(
                        text = "Cancel"
                    )
                }
            }
        )
    }

    error?.let { errorMessage ->
        LaunchedEffect(errorMessage) {
            viewModel.clearError()
        }
    }
}