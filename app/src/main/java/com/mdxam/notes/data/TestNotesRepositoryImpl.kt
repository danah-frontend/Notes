//package com.mdxam.notes.data
//
//import com.mdxam.notes.domain.Note
//import com.mdxam.notes.domain.NotesRepository
//import kotlinx.coroutines.flow.Flow
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.asStateFlow
//import kotlinx.coroutines.flow.forEach
//import kotlinx.coroutines.flow.map
//import kotlinx.coroutines.flow.update
//
//object TestNotesRepositoryImpl : NotesRepository {
//
//    private val testData = mutableListOf<Note>().apply{
//        repeat(10){
//            add(Note(it,"Title: $it","Content: $it", System.currentTimeMillis(),false))
//        }
//    }
//    private val notesListFlow = MutableStateFlow<List<Note>>(testData)
//
//    override fun addNote(title: String, content: String) {
//        //        notesListFlow.update{
////            it.toMutableList().apply {
////                add(note)
////            }
////        }
//        notesListFlow.update{oldList ->
//            val note = Note(
//                id = oldList.size,
//                title = title,
//                content = content,
//                updatedAt = System.currentTimeMillis(),
//                isPinned = false
//            )
//            oldList + note
//        }
//    }
//
//
//    override fun deleteNote(noteId: Int) {
//        notesListFlow.update {oldList ->
//            oldList.toMutableList().apply {
//                removeIf { newList -> newList.id == noteId }
//            }
//        }
//    }
//
//    override fun editNote(note: Note) {
//        notesListFlow.update {oldList ->
//            oldList.map {
//                if (it.id == note.id){
//                    note
//                }else{
//                    it
//                }
//            }
//        }
//    }
//
//    override fun getAllNotes(): Flow<List<Note>> {
//        return notesListFlow.asStateFlow()
//    }
//
//    override fun getNote(noteId: Int): Note {
//        return notesListFlow.value.first{
//            it.id == noteId
//        }
//    }
//
//    override fun searchNotes(query: String): Flow<List<Note>> {
//        return notesListFlow.map {currentList ->
//            currentList.filter {
//                it.title.contains(query) || it.content.contains(query)
//            }
//        }
//    }
//
//    override fun switchPinnedStatus(noteId: Int) {
//        notesListFlow.update {oldList ->
//            oldList.map {
//                if (it.id == noteId){
//                    it.copy(isPinned = !it.isPinned)
//                }else{
//                    it
//                }
//            }
//        }
//    }
//}