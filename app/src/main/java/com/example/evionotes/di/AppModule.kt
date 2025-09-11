package com.example.evionotes.di

import android.content.Context
import androidx.room.Room
import com.example.evionotes.data.local.AppDatabase
import com.example.evionotes.data.local.NoteDAO
import com.example.evionotes.data.local.UserDAO
import com.example.evionotes.data.security.CryptoManager
import com.example.evionotes.data.security.KeyStoreHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    private const val DATABASE_NAME = "evionotes_db"

    @Provides
    @Singleton
    fun provideKeyStoreHelper(): KeyStoreHelper = KeyStoreHelper()

    @Provides
    @Singleton
    fun provideCryptoManager(keyStoreHelper: KeyStoreHelper): CryptoManager =
        CryptoManager(keyStoreHelper.getOrCreateSecretKey())

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): AppDatabase =
        Room.databaseBuilder(context = appContext, klass = AppDatabase::class.java, name = DATABASE_NAME).fallbackToDestructiveMigration().build()

    @Provides
    @Singleton
    fun provideUserDao(database: AppDatabase): UserDAO = database.userDao()

    @Provides
    @Singleton
    fun provideNoteDao(database: AppDatabase): NoteDAO = database.noteDao()
}