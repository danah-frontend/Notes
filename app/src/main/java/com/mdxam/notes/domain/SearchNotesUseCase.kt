package com.mdxam.notes.domain

import kotlinx.coroutines.flow.Flow

class SearchNotesUseCase(
    private val repository: NotesRepository
) {
    operator fun invoke(query: String): Flow<List<Note>> {
        return repository.searchNotes(query)
    }
}