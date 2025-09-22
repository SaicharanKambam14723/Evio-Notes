package com.example.evionotes.ui.notes.list


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.evionotes.data.local.ChecklistItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteDetailScreen(
    noteId: Int,
    onBackClick: () -> Unit,
    viewModel: NoteDetailViewModel = hiltViewModel(),
) {
    val noteState by viewModel.note.collectAsState()

    var title by remember(noteState?.id) { mutableStateOf(noteState?.title ?: "") }
    var content by remember(noteState?.id) { mutableStateOf(noteState?.content ?: "") }

    val isLoading by viewModel.isLoading.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(noteId) {
        viewModel.loadNoteById(noteId)
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Note Detail",
                        color = Color(0xFF193872),
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBackClick
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color(0xFF193872)
                        )
                    }
                },
                actions = {
                    TextButton(
                        onClick = {
                            viewModel.updateTitle(title)
                            viewModel.updateContent(content)
                            viewModel.saveNote()
                        }
                    ) {
                        Text(
                            text = "Save",
                            color = Color(0xFF5CB9EC)
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        snackbarHost = {
            SnackbarHost(snackbarHostState)
        }
    ) { padding ->
        if(isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
            return@Scaffold
        }
        noteState?.let { note ->
            val scrollState = rememberScrollState()
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(padding)
                    .padding(20.dp)
                    .background(
                        brush = Brush
                            .verticalGradient(
                                colors = listOf(
                                    Color.White,
                                    Color(0xFF5CB9EC).copy(alpha = 0.09f)
                                )
                            )
                    )
            ) {
                TextField(
                    value = title,
                    onValueChange = {
                        title = it
                    },
                    label = { Text("Title", color = Color(0xFF193872)) },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = when(note.type) {
                        NoteType.Regular -> 2
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))
                when {
                    note.imageUrls?.isNotEmpty() == true -> {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            note.imageUrls.forEach { imageUrl ->
                                Image(
                                    painter = painterResource(id = imageUrl),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(180.dp)
                                        .clip(MaterialTheme.shapes.medium)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
                when(note.type) {
                    NoteType.Checklist -> {
                        var checklistItems by remember {
                            mutableStateOf(
                                note.checklistItems ?: emptyList()
                            )
                        }
                        checklistItems.forEachIndexed { index, (label, checked) ->
                            Row(
                                modifier = Modifier.padding(vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Checkbox(
                                    checked = checked,
                                    onCheckedChange = {
                                        checklistItems = checklistItems.toMutableList().also {
                                            it[index] = it[index].copy(checked = !checked)
                                        }
                                    }
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                TextField(
                                    value = label,
                                    onValueChange = { newLabel ->
                                        checklistItems = checklistItems.toMutableList().also {
                                            it[index] = it[index].copy(label = newLabel)
                                        }
                                    },
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = {
                                (checklistItems + ("" to false)).also { checklistItems =
                                    it as List<ChecklistItem>
                                }
                            }
                        ) {
                            Text("Add Item")
                        }

                        DisposableEffect(checklistItems) {
                            onDispose {
                                viewModel.updateChecklistItems(checklistItems)
                            }
                        }
                    }
                    else -> {
                        TextField(
                            value = content,
                            onValueChange = {
                                content = it
                            },
                            label = {
                                Text("Content")
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(150.dp),
                            maxLines = Int.MAX_VALUE
                        )
                    }
                }
            }
        }
    }
}