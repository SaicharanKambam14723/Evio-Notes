package com.example.evionotes.data.repository

import com.example.evionotes.data.local.NoteDAO
import com.example.evionotes.data.local.NoteEntity
import com.example.evionotes.data.security.CryptoManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.security.PrivateKey
import javax.inject.Inject


class NoteRepository @Inject constructor(
    private val noteDao: NoteDAO,
    private val cryptoManager: CryptoManager
) {
    suspend fun upsertNote(note: NoteEntity) {
        val encryptedContent = cryptoManager.encrypt(note.content)
        val encryptedNote = note.copy(content = encryptedContent)
        if (note.id == 0) {
            noteDao.insertNote(encryptedNote)
        } else {
            noteDao.updateNote(encryptedNote)
        }
    }

    fun getNotesForUser(userId: Int): Flow<List<NoteEntity>> =
        noteDao.getNotesForUser(userId).map { noteList ->
            noteList.map { note ->
                note.copy(content = cryptoManager.decrypt(note.content))
            }
        }

    suspend fun getNoteById(noteId: Int): NoteEntity? {
        val note = noteDao.getNoteById(noteId)
        return note?.copy(content = cryptoManager.decrypt(note.content))
    }

    suspend fun deleteNote(note: NoteEntity) {
        noteDao.deleteNote(note)
    }
}