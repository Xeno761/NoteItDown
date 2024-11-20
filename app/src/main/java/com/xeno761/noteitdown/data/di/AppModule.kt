package com.xeno761.noteitdown.data.di

import android.content.Context
import androidx.room.Room
import com.xeno761.noteitdown.data.local.Database
import com.xeno761.noteitdown.data.local.MIGRATION_1_2
import com.xeno761.noteitdown.data.repository.DataStoreRepositoryImpl
import com.xeno761.noteitdown.data.repository.FolderRepositoryImpl
import com.xeno761.noteitdown.data.repository.NoteRepositoryImpl
import com.xeno761.noteitdown.domain.usecase.AddFolder
import com.xeno761.noteitdown.domain.repository.NoteRepository
import com.xeno761.noteitdown.domain.usecase.AddNote
import com.xeno761.noteitdown.domain.usecase.DeleteFolder
import com.xeno761.noteitdown.domain.usecase.DeleteNote
import com.xeno761.noteitdown.domain.usecase.DeleteNotesByFolderId
import com.xeno761.noteitdown.domain.usecase.GetFolders
import com.xeno761.noteitdown.domain.usecase.GetNotes
import com.xeno761.noteitdown.domain.usecase.UseCases
import com.xeno761.noteitdown.domain.usecase.SearchNotes
import com.xeno761.noteitdown.domain.usecase.UpdateFolder
import com.xeno761.noteitdown.domain.usecase.UpdateNote
import com.xeno761.noteitdown.domain.repository.DataStoreRepository
import com.xeno761.noteitdown.domain.repository.FolderRepository
import com.xeno761.noteitdown.domain.usecase.GetNoteById
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideDataStoreRepository(@ApplicationContext context: Context): DataStoreRepository =
        DataStoreRepositoryImpl(context)

    @Provides
    @Singleton
    fun provideNoteDatabase(@ApplicationContext context: Context): Database =
        Room.databaseBuilder(
            context,
            Database::class.java,
            Database.NAME
        ).addMigrations(MIGRATION_1_2).build()

    @Provides
    @Singleton
    fun provideNoteRepository(database: Database): NoteRepository =
        NoteRepositoryImpl(dao = database.noteDao)

    @Provides
    @Singleton
    fun provideFolderRepository(database: Database): FolderRepository =
        FolderRepositoryImpl(dao = database.folderDao)

    @Provides
    @Singleton
    fun provideNoteUseCases(
        noteRepository: NoteRepository,
        folderRepository: FolderRepository
    ): UseCases = UseCases(
        getNotes = GetNotes(noteRepository),
        getNoteById = GetNoteById(noteRepository),
        deleteNote = DeleteNote(noteRepository),
        addNote = AddNote(noteRepository),
        searchNotes = SearchNotes(noteRepository),
        updateNote = UpdateNote(noteRepository),
        deleteNotesByFolderId = DeleteNotesByFolderId(noteRepository),
        addFolder = AddFolder(folderRepository),
        updateFolder = UpdateFolder(folderRepository),
        deleteFolder = DeleteFolder(folderRepository),
        getFolders = GetFolders(folderRepository)
    )
}
