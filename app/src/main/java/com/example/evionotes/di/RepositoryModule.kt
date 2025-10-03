package com.example.evionotes.di

import com.example.evionotes.data.repository.NoteRepositoryImpl
import com.example.evionotes.data.repository.UserRepositoryImpl
import com.example.evionotes.domain.repository.NoteRepository
import com.example.evionotes.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bidUserRepository(
        userRepositoryImpl: UserRepositoryImpl
    ) : UserRepository

    @Binds
    @Singleton
    abstract fun bindNotesRepository(
        noteRepositoryImpl: NoteRepositoryImpl
    ) : NoteRepository
}