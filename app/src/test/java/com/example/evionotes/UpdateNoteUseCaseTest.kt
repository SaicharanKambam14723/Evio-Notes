package com.example.evionotes

import com.example.evionotes.data.local.NoteEntity
import com.example.evionotes.data.repository.NoteRepository
import com.example.evionotes.domain.usecase.UpdateNoteUseCase
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.verify

class UpdateNoteUseCaseTest {
    private lateinit var noteRepository: NoteRepository
    private lateinit var updateNoteUseCase: UpdateNoteUseCase

    @Before
    fun setup() {
        noteRepository = mock()
        updateNoteUseCase = UpdateNoteUseCase(noteRepository)
    }

    @Test
    fun `invoke calls repository updateNote`() = runBlocking {
        val note = NoteEntity(id = 1, title = "title", content = "content", timestamp = System.currentTimeMillis(), userId = 1)

        updateNoteUseCase(note)
        verify(noteRepository).updateNote(note)
    }
}