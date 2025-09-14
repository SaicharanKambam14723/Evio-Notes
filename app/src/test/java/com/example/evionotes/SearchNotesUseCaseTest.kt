package com.example.evionotes

import com.example.evionotes.data.local.NoteEntity
import com.example.evionotes.data.repository.NoteRepository
import com.example.evionotes.domain.usecase.SearchNotesUseCase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class SearchNotesUseCaseTest {
    private lateinit var noteRepository: NoteRepository
    private lateinit var searchNotesUseCase: SearchNotesUseCase

    @Before
    fun setup() {
        noteRepository = mock()
        searchNotesUseCase = SearchNotesUseCase(noteRepository)
    }

    @Test
    fun `invoke filters notes by query`() = runBlocking {
        val allNotes = listOf(
            NoteEntity(id = 1,  "Buy milk", "Remember to buy milk", 1000L, 1),
            NoteEntity(2, "Work", "Finish project", 2000L,  1),
            NoteEntity(3, "Milk recipe", "Use fresh milk", 3000L, 2)
        )
        whenever(noteRepository.getNotesForUser(1)).thenReturn(flowOf(allNotes))

        val result = searchNotesUseCase("milk", 1).first()

        assertEquals(2, result.size)
        assert(result.all { it.title.contains("milk", true) || it.content.contains("milk", true) })
    }
}