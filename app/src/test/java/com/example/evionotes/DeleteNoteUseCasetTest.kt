package com.example.evionotes

import com.example.evionotes.data.local.NoteEntity
import com.example.evionotes.data.repository.NoteRepository
import com.example.evionotes.domain.usecase.DeleteNoteUseCase
import com.example.evionotes.ui.notes.list.NoteType
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.verify

class DeleteNoteUseCasetTest {
    private lateinit var noteRepository: NoteRepository
    private lateinit var deleteNoteUseCase: DeleteNoteUseCase

    @Before
    fun setup() {
        noteRepository = mock()
        deleteNoteUseCase = DeleteNoteUseCase(noteRepository)
    }

    @Test
    fun `invoke calls repository deleteNote`() = runBlocking {
        val note = NoteEntity(id = 1, title = "title", content = "content", timestamp = System.currentTimeMillis(), userId = 1,  type = NoteType.Regular, imageUrls = emptyList(), checklistItems = emptyList())
        deleteNoteUseCase.invoke(note)
        verify(noteRepository).deleteNote(note)
    }
}