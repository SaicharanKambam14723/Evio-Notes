package com.example.evionotes

import com.example.evionotes.data.local.NoteEntity
import com.example.evionotes.data.local.ChecklistItem
import com.example.evionotes.data.repository.NoteRepository
import com.example.evionotes.domain.usecase.SearchNotesUseCase
import com.example.evionotes.ui.notes.list.NoteType
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
            NoteEntity(
                id = 1,
                title = "Buy milk",
                content = "Remember to buy milk",
                type = NoteType.Regular,
                imageUrls = emptyList(),
                checklistItems = emptyList<ChecklistItem>(),
                timestamp = 1000L,
                userId = 1
            ),
            NoteEntity(
                id = 2,
                title = "Work",
                content = "Finish project",
                type = NoteType.Regular,
                imageUrls = emptyList(),
                checklistItems = emptyList(),
                timestamp = 2000L,
                userId = 1
            ),
            NoteEntity(
                id = 3,
                title = "Milk recipe",
                content = "Use fresh milk",
                type = NoteType.Regular,
                imageUrls = emptyList(),
                checklistItems = emptyList(),
                timestamp = 3000L,
                userId = 2
            )
        )

        // Mock the repository to return the notes for userId 1
        whenever(noteRepository.getNotesForUser(1)).thenReturn(flowOf(allNotes.filter { it.userId == 1 }))

        // Invoke the use case with query "milk" and userId 1
        val result = searchNotesUseCase("milk", 1).first()

        // Assert that 1 note is returned (only notes for userId 1 are considered)
        assertEquals(1, result.size)

        // Assert that the returned notes contain the query in title or content
        assert(result.all {
            it.title?.contains("milk", ignoreCase = true) == true ||
                    it.content?.contains("milk", ignoreCase = true) == true
        })
    }
}
