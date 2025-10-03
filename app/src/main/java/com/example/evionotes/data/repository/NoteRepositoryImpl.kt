package com.example.evionotes.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.example.evionotes.data.local.dao.NoteDAO
import com.example.evionotes.data.local.entity.NoteEntity
import com.example.evionotes.util.CryptoUtil
import com.example.evionotes.domain.model.Note
import com.example.evionotes.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NoteRepositoryImpl @Inject constructor(
    private val noteDao: NoteDAO,
    private val cryptoUtil: CryptoUtil
) : NoteRepository {
    override suspend fun getNotes(userId: String): Flow<PagingData<Note>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { noteDao.getNotesPagingSource(userId) }
        ).flow.map { pagingData ->
            pagingData.map { entity ->
                entity.toNote(cryptoUtil)
            }
        }
    }


    override fun getNotesFlow(userId: String): Flow<List<Note>> {
        return noteDao.getNotesFlow(userId).map { entities ->
            entities.map {  note ->
                note.toNote(cryptoUtil)
            }
        }
    }

    override suspend fun getNoteById(noteId: String): Note? {
        return noteDao.getNoteById(noteId)?.toNote(cryptoUtil)
    }

    override fun getArchivedNotes(userId: String): Flow<List<Note>> {
        return noteDao.getArchivedNotes(userId).map { entities ->
            entities.map { note ->
                note.toNote(cryptoUtil)
            }
        }
    }

    override fun searchNotes(userId: String, query: String): Flow<List<Note>> {
        return noteDao.searchNotes(userId, "%$query%").map { entities ->
            entities.map { note ->
                note.toNote(cryptoUtil)
            }
        }
    }

    override suspend fun insertNote(note: Note, userId: String): Result<Unit> {
        return try {
            val encryptedContent = cryptoUtil.encrypt(note.content)
            val entity = NoteEntity(
                id = note.id.ifEmpty { UUID.randomUUID().toString() },
                userId = userId,
                title = note.title,
                content = note.content,
                encryptedContent = encryptedContent,
                tags = note.tags,
                color = note.color,
                isPinned = note.isPinned,
                isArchived = note.isArchived,
                createdAt = note.createdAt,
                updatedAt = System.currentTimeMillis(),
                imageUrls = note.imageUrls
            )
            noteDao.insertNote(entity)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateNote(note: Note): Result<Unit> {
        return try {
            val existingEntity = noteDao.getNoteById(note.id)?: return Result.failure(Exception("Note not found"))
            val encryptedContent = cryptoUtil.encrypt(note.content)
            val updatedEntity = existingEntity.copy(
                title = note.title,
                content = note.content,
                encryptedContent = encryptedContent,
                tags = note.tags,
                color = note.color,
                isPinned = note.isPinned,
                isArchived = note.isArchived,
                updatedAt = System.currentTimeMillis(),
                imageUrls = note.imageUrls
            )
            noteDao.updateNote(updatedEntity)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteNote(noteId: String): Result<Unit> {
        return try {
            val note = noteDao.getNoteById(noteId) ?: return Result.failure(Exception("Note not found"))
            noteDao.deleteNote(note)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun togglePinNote(noteId: String): Result<Unit> {
        return try {
            val note = noteDao.getNoteById(noteId) ?: return Result.failure(Exception("Note not found"))
            noteDao.updateNotePinnedStatus(noteId, !note.isPinned)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun toggleArchiveNote(noteId: String): Result<Unit> {
        return try {
            val note = noteDao.getNoteById(noteId) ?: return Result.failure(Exception("Note not found"))
            noteDao.updateNotePinnedStatus(noteId, !note.isArchived)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

private fun NoteEntity.toNote(cryptoUtil: CryptoUtil): Note {
    val decryptedContent = try {
        cryptoUtil.decrypt(encryptedContent)
    } catch (e: Exception) {
        content
    }

    return Note(
        id = id,
        title = title,
        content = decryptedContent,
        tags = tags,
        color = color,
        isPinned = isPinned,
        isArchived = isArchived,
        createdAt = createdAt,
        updatedAt = updatedAt,
        imageUrls = imageUrls
    )
}

