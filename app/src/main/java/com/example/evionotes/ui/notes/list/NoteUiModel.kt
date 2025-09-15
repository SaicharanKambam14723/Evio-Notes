package com.example.evionotes.ui.notes.list

import com.example.evionotes.data.local.ChecklistItem

enum class NoteType {
    Regular,
    Checklist,
    Markdown,
    Quote,
    Highlight,
    Strikethrough,
    Code,
    Image,
    Mixed
}

data class NoteUiModel(
    val id: Int,
    val title: String? = null,
    val content: String? = null,
    val type: NoteType = NoteType.Regular,
    val checklistItems: List<ChecklistItem>?,
    val imageUrls: List<Int>?,
    val timestamp: Long? = null,
    val userId: Int
)