package com.example.evionotes.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.evionotes.ui.notes.list.NoteType
import kotlinx.serialization.Serializable

@Serializable
data class ChecklistItem(
    val label: String,
    val checked: Boolean
)

@Entity(tableName = "notes")
data class NoteEntity (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "note_id")
    val id: Int = 0,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "content")
    val content: String,
    @ColumnInfo(name = "type")
    val type: NoteType,
    @ColumnInfo(name = "images")
    val imageUrls: List<Int>?,
    @ColumnInfo(name = "checklists")
    val checklistItems: List<ChecklistItem>?,
    @ColumnInfo(name = "timestamp")
    val timestamp: Long,
    @ColumnInfo(name = "user_id")
    val userId: Int
)