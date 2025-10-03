package com.example.evionotes.di

import android.content.Context
import androidx.room.Room
import com.example.evionotes.data.local.dao.NoteDAO
import com.example.evionotes.data.local.dao.UserDAO
import com.example.evionotes.data.local.database.NoteDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideNotesDatabase(
        @ApplicationContext context: Context
    ): NoteDatabase {
        return Room.databaseBuilder(
            context,
            NoteDatabase::class.java,
            NoteDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    fun provideUserDAO(database: NoteDatabase): UserDAO {
        return database.userDao()
    }

    @Provides
    fun provideNoteDAO(database: NoteDatabase): NoteDAO {
        return database.noteDao()
    }
}