package com.example.evionotes.ui.notes.list

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
    val checklistItems: List<Pair<String, Boolean>>?=null,
    val imageUrls: List<Int> = emptyList(),
    val timestamp: Long? = null
)