package com.example.evionotes.domain.usecase

import com.example.evionotes.data.local.NoteEntity
import com.example.evionotes.data.repository.NoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SearchNotesUseCase  @Inject constructor(
    private val noteRepository: NoteRepository
) {
    operator fun invoke(query: String, userId: String): Flow<List<NoteEntity>> {
        return noteRepository.getNotesForUser(userId)
            .map { notes->
                notes.filter { note->
                    note.title.contains(query, ignoreCase = true) || note.content.contains(query, ignoreCase = true)
                }
            }
    }
}