package com.example.evionotes.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.evionotes.data.local.entity.NoteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDAO {
    @Query("SELECT * FROM notes WHERE userId = :userId AND isArchived = 0 ORDER BY isPinned DESC, updatedAt DESC")
    fun getNotesPagingSource(userId: String): PagingSource<Int, NoteEntity>

    @Query("SELECT * FROM notes WHERE userId = :userId AND isArchived = 0 ORDER BY isPinned DESC, updatedAt DESC")
    fun getNotesFlow(userId: String): Flow<List<NoteEntity>>

    @Query("SELECT * FROM notes WHERE id = :noteId")
    suspend fun getNoteById(noteId: String): NoteEntity?

    @Query("SELECT * FROM notes WHERE userId = :userId AND isArchived = 1 ORDER BY updatedAt DESC")
    fun getArchivedNotes(userId: String): Flow<List<NoteEntity>>

    @Query("SELECT * FROM notes WHERE userId = :userId AND (title LIKE :query OR content LIKE :query) ORDER BY updatedAt DESC")
    fun searchNotes(userId: String, query: String): Flow<List<NoteEntity>>

    @Insert
    suspend fun insertNote(note: NoteEntity)

    @Update
    suspend fun updateNote(note: NoteEntity)

    @Delete
    suspend fun deleteNote(note: NoteEntity)

    @Query("DELETE FROM notes WHERE userId = :userId")
    suspend fun deleteAllUserNotes(userId: String)

    @Query("UPDATE notes SET isPinned = :isPinned WHERE id = :noteId")
    suspend fun updateNotePinnedStatus(noteId: String, isPinned: Boolean)

    @Query("UPDATE notes SET isArchived = :isArchived WHERE id = :noteId")
    suspend fun updateNoteArchivedStatus(noteId: String, isArchived: Boolean)
}