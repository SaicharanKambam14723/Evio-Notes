package com.example.evionotes.data.local.database


import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.evionotes.data.local.converter.Converters
import com.example.evionotes.data.local.dao.NoteDAO
import com.example.evionotes.data.local.dao.UserDAO
import com.example.evionotes.data.local.entity.NoteEntity
import com.example.evionotes.data.local.entity.UserEntity


@Database(
    entities = [NoteEntity::class, UserEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class NoteDatabase: RoomDatabase() {
    abstract fun noteDao(): NoteDAO
    abstract fun userDao(): UserDAO

    companion object {
        const val DATABASE_NAME = "EvioNotes_DB"
    }
}