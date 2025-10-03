package com.example.evionotes.presentation.viewmodel

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.evionotes.domain.model.Note
import com.example.evionotes.domain.model.User
import com.example.evionotes.domain.repository.NoteRepository
import com.example.evionotes.domain.repository.UserRepository
import com.example.evionotes.domain.use_case.notes.SaveNotesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteDetailViewModel @Inject constructor(
    private val noteRepository: NoteRepository,
    private val userRepository: UserRepository,
    private val saveNotesUseCase: SaveNotesUseCase
): ViewModel() {
    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    private val _note = MutableStateFlow<Note?>(null)
    val note: StateFlow<Note?> = _note.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _isSaved = MutableStateFlow(false)
    val isSaved: StateFlow<Boolean> = _isSaved.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        observeCurrentUser()
    }

    private fun observeCurrentUser() {
        viewModelScope.launch {
            userRepository.getLoggedInUser().collect { user ->
                _currentUser.value = user
            }
        }
    }

    fun loadNote(noteId: String) {
        if(noteId.isNotEmpty()) {
            viewModelScope.launch {
                _isLoading.value = true
                val note = noteRepository.getNoteById(noteId)
                if(note != null) {
                    _note.value = note
                }
            }
        }
    }

    fun updateTitle(title: String) {
        _note.value = _note.value?.copy(title = title)
    }

    fun updateContent(content: String) {
        _note.value = _note.value?.copy(content = content)
    }

    fun updateColor(color: Color) {
        _note.value = _note.value?.copy(color = color.toArgb())
    }

    fun togglePin() {
        _note.value = _note.value?.copy(isPinned = !_note.value!!.isPinned)
    }

    fun addTag(tag: String) {
        val currentTags = _note.value?.tags?.toMutableList()

        if (!currentTags?.contains(tag)!! && tag.isNotBlank()) {
            currentTags.add(tag)
            _note.value = _note.value?.copy(tags = currentTags)
        }
    }

    fun removeTag(tag: String) {
        val currentTags = _note.value?.tags?.toMutableList()

        currentTags?.remove(tag)
        _note.value = currentTags?.let { _note.value?.copy(tags = it) }
    }

    fun addImage(imageUri: String) {
        val currentImages = _note.value?.imageUrls?.toMutableList()
        currentImages?.add(imageUri)
        _note.value = currentImages?.let { _note.value?.copy(imageUrls = it) }
    }

    fun removeImage(imageUri: String) {
        val currentImages = _note.value?.imageUrls?.toMutableList()
        currentImages?.remove(imageUri)
        _note.value = currentImages?.let { _note.value?.copy(imageUrls = it) }
    }

    fun saveNote() {
        val user = _currentUser.value ?: return

        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            _note.value?.let { saveNotesUseCase(it, user.id) }
                ?.onSuccess {
                    _isSaved.value = true
                }
                ?.onFailure {  exception ->
                    _error.value = exception.message
                }

            _isLoading.value = false
        }
    }

    fun clearError() {
        _error.value = null
    }

    fun resetSaveStatus() {
        _isSaved.value = false
    }
}