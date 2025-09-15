package com.example.evionotes.domain.usecase

import com.example.evionotes.data.repository.NoteRepository
import com.example.evionotes.ui.notes.list.NoteUiModel
import com.example.evionotes.ui.notes.list.toUiModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class GetNotesUseCase @Inject constructor(private val repository: NoteRepository) {
    operator fun invoke(userId: Int): Flow<List<NoteUiModel>> =
        repository.getNotesForUser(userId).map { entities ->
            entities.map { it.toUiModel() }
        }
}