package com.example.evionotes.ui.notes.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.evionotes.data.local.ChecklistItem
import com.example.evionotes.data.local.NoteEntity
import com.example.evionotes.data.repository.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteDetailViewModel @Inject constructor(
    private val repository: NoteRepository
): ViewModel() {
    private val _note = MutableStateFlow<NoteUiModel?>(null)
    val note: StateFlow<NoteUiModel?> = _note.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun loadNoteById(noteId: Int) {
        if(noteId <= 0) {
            _note.value = NoteUiModel(
                id = 0,
                title = "",
                content = "",
                type = NoteType.Regular,
                imageUrls = emptyList(),
                checklistItems = emptyList(),
                timestamp = System.currentTimeMillis(),
                userId = 1
            )
            _isLoading.value = false
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            val noteEntity: NoteEntity? = repository.getNoteById(noteId)
            _note.value = noteEntity?.toUiModel()
            _isLoading.value = false
        }
    }

    fun saveNote() {
        viewModelScope.launch {
            _note.value?.let {  noteUi ->
                repository.upsertNote(noteUi.toEntity())
            }
        }
    }

    fun updateContent(newContent: String) {
        _note.value = _note.value?.copy(content = newContent)
    }

    fun updateChecklistItems(newItems: List<ChecklistItem>) {
        _note.value = _note.value?.copy(checklistItems = newItems)
    }

    fun updateTitle(title: String) {
        _note.value = _note.value?.copy(title = title)
    }

}