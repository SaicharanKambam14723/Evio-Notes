package com.example.evionotes

import com.example.evionotes.data.local.NoteEntity
import com.example.evionotes.data.repository.NoteRepository
import com.example.evionotes.domain.usecase.AddNoteUseCase
import com.example.evionotes.ui.notes.list.NoteType
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.verify

class AddNoteUseCaseTest {
    private lateinit var noteRepository: NoteRepository
    private lateinit var  addNoteUseCase: AddNoteUseCase

    @Before
    fun setup() {
        noteRepository = mock()
        addNoteUseCase = AddNoteUseCase(noteRepository)
    }

    @Test
    fun `invoke calls repository upsertNote`() = runBlocking {
        val note = NoteEntity(id = 0, userId = 1, title = "Test", content = "Content", timestamp = System.currentTimeMillis(), type = NoteType.Regular, imageUrls = emptyList(), checklistItems = emptyList())
        addNoteUseCase.invoke(note)
        verify(noteRepository).upsertNote(note)
    }
}