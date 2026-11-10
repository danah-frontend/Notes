package com.mdxam.notes.domain

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllNotesUseCase @Inject constructor(
    private val repository: NotesRepository
) {

    operator fun invoke(): Flow<List<Note>> {
        return repository.getAllNotes()
    }
}