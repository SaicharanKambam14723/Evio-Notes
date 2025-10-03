package com.example.evionotes.domain.use_case.notes

import androidx.paging.PagingData
import com.example.evionotes.domain.model.Note
import com.example.evionotes.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetNotesUseCase @Inject constructor(
    private val noteRepository: NoteRepository
) {
    suspend operator fun invoke(userId: String): Flow<PagingData<Note>> {
        return noteRepository.getNotes(userId)
    }
}