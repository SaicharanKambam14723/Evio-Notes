package com.example.evionotes.domain.usecase

import com.example.evionotes.data.local.NoteEntity
import com.example.evionotes.data.repository.NoteRepository
import javax.inject.Inject

class DeleteNoteUseCase @Inject constructor(
    private val noteRepository: NoteRepository
) {
    suspend operator fun invoke(note: NoteEntity) {
        noteRepository.deleteNote(note)
    }
}