package com.mdxam.notes.di

import android.content.Context
import com.mdxam.notes.data.NotesDao
import com.mdxam.notes.data.NotesDataBase
import com.mdxam.notes.data.NotesRepositoryImpl
import com.mdxam.notes.domain.NotesRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Singleton
    @Binds
    fun bindNotesRepository(
        impl: NotesRepositoryImpl
    ): NotesRepository

    companion object {
        @Singleton
        @Provides
        fun provideDataBase(
            @ApplicationContext context : Context
        ): NotesDataBase{
            return NotesDataBase.getInstance(context)
        }

        @Singleton
        @Provides
        fun provideNoteDao(
            dataBase : NotesDataBase
        ): NotesDao{
            return dataBase.notesDao()
        }
    }

}