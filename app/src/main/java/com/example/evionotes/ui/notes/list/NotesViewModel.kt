@file:OptIn(ExperimentalCoroutinesApi::class)

package com.example.evionotes.ui.notes.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.evionotes.domain.usecase.GetNotesUseCase
import com.example.evionotes.domain.usecase.SearchNotesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class UiEvent {
    data class NavigateToNote(val noteId: Int) : UiEvent()
    object NavigateToAddNote : UiEvent()
    data class ShowImageGallery(val images: List<Int>) : UiEvent()
}

@HiltViewModel
class NotesViewModel @Inject constructor(
    private val getNotesUseCase: GetNotesUseCase,
    private val searchNotesUseCase: SearchNotesUseCase
) : ViewModel() {
    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _userId = MutableStateFlow(1)

    private val _events = Channel<UiEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    val notes = combine(_userId, _searchQuery) { userId, query ->
        userId to query
    }.flatMapLatest { (userId, query) ->
        if (query.isBlank()) {
            getNotesUseCase(userId)
        } else {
            searchNotesUseCase(query, userId)  // Corrected parameter order here
        }
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    fun onAddNoteClicked() = viewModelScope.launch {
        _events.send(UiEvent.NavigateToAddNote)
    }

    fun onNoteSelected(note: NoteUiModel) = viewModelScope.launch {
        _events.send(UiEvent.NavigateToNote(note.id))
    }

    fun onViewImages(images: List<Int>) = viewModelScope.launch {
        _events.send(UiEvent.ShowImageGallery(images))
    }
}
