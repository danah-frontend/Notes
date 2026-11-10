package com.mdxam.notes.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.mdxam.notes.presentation.screens.creation.CreateNoteScreen
import com.mdxam.notes.presentation.screens.editing.EditNoteScreen
import com.mdxam.notes.presentation.screens.notes.NotesScreen
import kotlinx.serialization.Serializable

@Composable
fun NavGraph(){
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Screen.Notes
    ){
        composable<Screen.Notes> {
            NotesScreen(
                onNoteClick = {note ->
                    navController.navigate(Screen.EditNote(noteId = note.id))
                },
                onAddNoteClick = {
                    navController.navigate(Screen.CreateNote)
                }
            )
        }
        composable<Screen.CreateNote> {
            CreateNoteScreen(
                onFinished = {
                    navController.popBackStack()
                }
            )
        }
        composable<Screen.EditNote> {backStackEntry ->
            val editNoteRoute: Screen.EditNote = backStackEntry.toRoute()
            EditNoteScreen(
                noteId = editNoteRoute.noteId,
                onFinished = {
                    navController.popBackStack()
                }
            )
        }
    }
}

@Serializable
sealed interface Screen{
    @Serializable
    data object Notes: Screen
    @Serializable
    data object CreateNote: Screen
    @Serializable
    data class EditNote(val noteId: Int): Screen
}


//@RequiresApi(Build.VERSION_CODES.O)
//@Composable
//fun CustomNavGraph(){
//
//    val screen = remember {mutableStateOf<CustomScreen>(CustomScreen.Notes)}
//
//    when(val currentScreen = screen.value){
//        CustomScreen.CreateNote -> {
//            CreateNoteScreen(
//                onFinished = {
//                    screen.value = CustomScreen.Notes
//                }
//            )
//        }
//        is CustomScreen.EditNote -> {
//            EditNoteScreen(
//                noteId = currentScreen.noteId,
//                onFinished = {
//                    screen.value = CustomScreen.Notes
//                }
//            )
//        }
//        CustomScreen.Notes -> {
//            NotesScreen(
//                onNoteClick = {
//                    screen.value = CustomScreen.EditNote(it.id)
//                },
//                onAddNoteClick = {
//                    screen.value = CustomScreen.CreateNote
//                }
//            )
//        }
//    }
//
//}
//
//
//sealed interface CustomScreen{
//
//    data object Notes: CustomScreen
//
//    data object CreateNote: CustomScreen
//
//    data class EditNote(
//        val noteId: Int
//    ): CustomScreen
//}