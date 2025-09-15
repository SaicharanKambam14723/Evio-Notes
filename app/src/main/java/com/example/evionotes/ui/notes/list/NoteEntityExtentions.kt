package com.example.evionotes.ui.notes.list

import com.example.evionotes.data.local.NoteEntity

fun NoteEntity.toUiModel(): NoteUiModel {
    return NoteUiModel(
        id = this.id,
        title = this.title,
        content = this.content,
        type = this.type,
        imageUrls = this.imageUrls?: emptyList(),
        checklistItems = this.checklistItems,
        timestamp = this.timestamp,
        userId = this.userId
    )
}

fun NoteUiModel.toEntity(): NoteEntity {
    return NoteEntity(
        id = this.id,
        title = this.title ?: "",
        content = this.content ?: "",
        type = this.type,
        imageUrls = this.imageUrls,
        checklistItems = this.checklistItems,
        timestamp = this.timestamp ?: 0L,
        userId = this.userId
    )
}