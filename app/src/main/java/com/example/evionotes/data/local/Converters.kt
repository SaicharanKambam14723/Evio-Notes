package com.example.evionotes.data.local

import androidx.room.TypeConverter
import com.example.evionotes.ui.notes.list.NoteType
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class Converters {
    private val json = Json { ignoreUnknownKeys = true }

    // List<Int> converter (for imageUrls)
    @TypeConverter
    fun fromIntList(value: List<Int>?): String? {
        return value?.let { json.encodeToString(it) }
    }

    @TypeConverter
    fun toIntList(value: String?): List<Int>? {
        return value?.let { json.decodeFromString(it) }
    }

    @TypeConverter
    fun fromChecklistList(value: List<ChecklistItem>?): String? {
        return value?.let { json.encodeToString(it) }
    }

    @TypeConverter
    fun toChecklistList(value: String?): List<ChecklistItem>? {
        return value?.let { json.decodeFromString(it) }
    }

    // NoteType enum converter
    @TypeConverter
    fun fromNoteType(value: NoteType): String {
        return value.name
    }

    @TypeConverter
    fun toNoteType(value: String): NoteType {
        return NoteType.valueOf(value)
    }
}
