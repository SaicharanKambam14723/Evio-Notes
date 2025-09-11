package com.example.evionotes.domain.usecase

import com.example.evionotes.data.local.NoteEntity
import com.example.evionotes.data.repository.NoteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetNoteUseCase @Inject constructor(
    private val noteRepository: NoteRepository
) {
    operator fun invoke(userId: Int): Flow<List<NoteEntity>> {
        return noteRepository.getNotesForUser(userId)
    }
}