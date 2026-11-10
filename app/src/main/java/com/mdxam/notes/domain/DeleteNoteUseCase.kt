package com.mdxam.notes.domain

import javax.inject.Inject


class DeleteNoteUseCase @Inject constructor(
    private val repository: NotesRepository
) {
    suspend operator fun invoke(noteId: Int) {
        repository.deleteNote(noteId)
    }
}