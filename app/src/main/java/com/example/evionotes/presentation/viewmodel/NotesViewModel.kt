package com.example.evionotes.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.evionotes.domain.model.Note
import com.example.evionotes.domain.model.User
import com.example.evionotes.domain.repository.NoteRepository
import com.example.evionotes.domain.repository.UserRepository
import com.example.evionotes.domain.use_case.notes.SaveNotesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(
    private val noteRepository: NoteRepository,
    private val userRepository: UserRepository,
    private val saveNotesUseCase: SaveNotesUseCase
): ViewModel() {

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _authState = MutableStateFlow<AuthState>(AuthState.Loading)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    val currentUser: StateFlow<User?> = authState
        .map { state ->
            if (state is AuthState.LoggedIn) state.user else null
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    val notes: StateFlow<PagingData<Note>> = currentUser
        .filterNotNull()
        .flatMapLatest { user ->
            noteRepository.getNotes(user.id).cachedIn(viewModelScope)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = PagingData.empty()
        )

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    val searchResults: StateFlow<List<Note>> = combine(
        currentUser.filterNotNull(),
        searchQuery.filter { it.isNotBlank() }
    ) { user, query ->
        noteRepository.searchNotes(user.id, query)
    }
        .flatMapLatest { it }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    init {
        fetchCurrentUser()
    }

    private fun fetchCurrentUser() {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            userRepository.getLoggedInUser().collect { user ->
                if (user != null) {
                    _authState.value = AuthState.LoggedIn(user)
                } else {
                    _authState.value = AuthState.LoggedOut
                }
            }
        }
    }


    fun searchNotes(query: String) {
        _searchQuery.value = query
    }

    fun clearSearch() {
        _searchQuery.value = ""
    }

    fun deleteNote(noteId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            noteRepository.deleteNote(noteId)
                .onFailure {  exception ->
                    _error.value = exception.message
                }

            _isLoading.value = false
        }

    }

    fun togglePinNote(noteId: String) {
        viewModelScope.launch {
            noteRepository.togglePinNote(noteId)
                .onFailure {  exception ->
                    _error.value = exception.message
                }
        }
    }

    fun toggleArchiveNote(noteId: String) {
        viewModelScope.launch {
            noteRepository.toggleArchiveNote(noteId)
                .onFailure {  exception ->
                    _error.value = exception.message
                }
        }
    }

    fun clearError() {
        _error.value = null
    }
}

