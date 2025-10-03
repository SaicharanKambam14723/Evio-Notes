package com.example.evionotes.domain.repository

import androidx.paging.PagingData
import com.example.evionotes.domain.model.Note
import kotlinx.coroutines.flow.Flow

interface NoteRepository {
    suspend fun getNotes(userId: String): Flow<PagingData<Note>>
    fun getNotesFlow(userId: String): Flow<List<Note>>
    suspend fun getNoteById(noteId: String): Note?
    fun getArchivedNotes(userId: String): Flow<List<Note>>
    fun searchNotes(userId: String, query: String): Flow<List<Note>>
    suspend fun insertNote(note: Note, userId: String): Result<Unit>
    suspend fun updateNote(note: Note): Result<Unit>
    suspend fun deleteNote(noteId: String): Result<Unit>
    suspend fun togglePinNote(noteId: String): Result<Unit>
    suspend fun toggleArchiveNote(noteId: String): Result<Unit>
}