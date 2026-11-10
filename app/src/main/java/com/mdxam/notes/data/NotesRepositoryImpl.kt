package com.mdxam.notes.data
import com.mdxam.notes.domain.Note
import com.mdxam.notes.domain.NotesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class NotesRepositoryImpl @Inject constructor(
    private val notesDao: NotesDao
): NotesRepository {
    override suspend fun addNote(
        title: String,
        content: String,
        isPinned: Boolean,
        updateAt: Long
    ) {
        val noteDbModel = NoteDbModel(0,title, content,updateAt,isPinned)

        notesDao.addNote(noteDbModel)
    }

    override suspend fun deleteNote(noteId: Int) {
        notesDao.deleteNote(noteId)
    }

    override suspend fun editNote(note: Note) {
        notesDao.addNote(note.toDbModel())
    }

    override fun getAllNotes(): Flow<List<Note>> {
        return notesDao.getAllNotes().map {noteList ->
            noteList.map {
                it.toEntity()
            }
        }
    }

    override suspend fun getNote(noteId: Int): Note {
        return notesDao.getNote(noteId).toEntity()
    }

    override fun searchNotes(query: String): Flow<List<Note>> {
        return notesDao.searchNotes(query).map {noteList ->
            noteList.map {
                it.toEntity()
            }
        }
    }

    override suspend fun switchPinnedStatus(noteId: Int) {
        notesDao.switchPinnedStatus(noteId)
    }
}