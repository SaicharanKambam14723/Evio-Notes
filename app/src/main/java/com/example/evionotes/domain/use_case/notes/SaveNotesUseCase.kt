package com.example.evionotes.domain.use_case.notes

import com.example.evionotes.domain.model.Note
import com.example.evionotes.domain.repository.NoteRepository
import javax.inject.Inject

class SaveNotesUseCase @Inject constructor(
    private val noteRepository: NoteRepository
) {
    suspend operator fun invoke(note: Note, userId: String): Result<Unit> {
        return if(note.id.isEmpty()) {
            noteRepository.insertNote(note, userId)
        } else {
            noteRepository.updateNote(note)
        }
    }
}