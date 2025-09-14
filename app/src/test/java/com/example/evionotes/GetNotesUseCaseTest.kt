package com.example.evionotes


import com.example.evionotes.data.local.NoteEntity
import com.example.evionotes.data.repository.NoteRepository
import com.example.evionotes.domain.usecase.GetNotesUseCase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class GetNotesUseCaseTest {
    private lateinit var noteRepository: NoteRepository
    private lateinit var getNotesUseCase: GetNotesUseCase


    @Before
    fun setup() {
        noteRepository = mock()
        getNotesUseCase = GetNotesUseCase(noteRepository)
    }

    @Test
    fun `invoke returns flow of notes for user`() = runBlocking {
        val noteList = listOf(
            NoteEntity(id = 1, title = "title", content = "content", timestamp = System.currentTimeMillis(), userId = 1),
            NoteEntity(id = 2, title = "title", content = "content", timestamp = System.currentTimeMillis(), userId = 1)
        )
        whenever(noteRepository.getNotesForUser(1)).thenReturn(flowOf(noteList))

        val result = getNotesUseCase.invoke(1).first()
        assertEquals(noteList, result)
    }
}