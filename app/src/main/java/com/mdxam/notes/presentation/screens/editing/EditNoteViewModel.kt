package com.mdxam.notes.presentation.screens.editing

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mdxam.notes.data.NotesRepositoryImpl
import com.mdxam.notes.domain.DeleteNoteUseCase
import com.mdxam.notes.domain.EditNoteUseCase
import com.mdxam.notes.domain.GetNoteUseCase
import com.mdxam.notes.domain.Note
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EditNoteViewModel(
    private val noteId: Int,
    context: Context
): ViewModel(){

    private val repository = NotesRepositoryImpl.getInstance(context)

    private val editNoteUseCase = EditNoteUseCase(repository)

    private val getNoteUseCase = GetNoteUseCase(repository)

    private val deleteNoteUseCase = DeleteNoteUseCase(repository)

    private val _state = MutableStateFlow<EditNoteState>(EditNoteState.Initial)

    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            _state.update {
                val note = getNoteUseCase(noteId)
                EditNoteState.Editing(note)
            }
        }
    }

    fun processCommand(command: EditNoteCommand){
        when(command){
            EditNoteCommand.Back -> {
                _state.update { EditNoteState.Finished }
            }
            is EditNoteCommand.InputContent -> {
                _state.update { previousState ->
                    if (previousState is EditNoteState.Editing){
                        val newNote = previousState.note.copy(content = command.content)
                        previousState.copy(note = newNote)
                    }else{
                        previousState
                    }
                }
            }
            is EditNoteCommand.InputTitle -> {
                _state.update { previousState ->
                if (previousState is EditNoteState.Editing){
                    val newNote = previousState.note.copy(title = command.title)
                    previousState.copy(note = newNote)
                }else{
                    previousState
                }
            }
            }
            EditNoteCommand.Save -> {
                viewModelScope.launch {
                    _state.update{previousState ->
                        if (previousState is EditNoteState.Editing){
                            val note = previousState.note
                            editNoteUseCase(note)
                            EditNoteState.Finished
                        }else{
                            previousState
                        }
                    }
                }
            }

            EditNoteCommand.Delete -> {
                viewModelScope.launch {
                    _state.update{previousState ->
                        if (previousState is EditNoteState.Editing){
                            val note = previousState.note
                            deleteNoteUseCase(note.id)
                            EditNoteState.Finished
                        }else{
                            previousState
                        }
                    }
                }
            }
        }
    }
}


sealed interface EditNoteCommand{

    data class InputTitle(
        val title: String
    ): EditNoteCommand

    data class InputContent(
        val content: String
    ): EditNoteCommand

    data object Save: EditNoteCommand

    data object Back: EditNoteCommand

    data object Delete: EditNoteCommand
}

sealed interface EditNoteState{

    data object Initial: EditNoteState

    data class Editing(
        val note: Note
    ): EditNoteState{
        val isSaveEnabled: Boolean
            get() = note.title.isNotBlank() && note.content.isNotBlank()
    }

    data object Finished: EditNoteState
}