package com.mdxam.notes.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(
    [NoteDbModel::class],
    version = 1,
    exportSchema = false
)
abstract class NotesDataBase: RoomDatabase(){

    abstract fun notesDao(): NotesDao

    companion object{

        private var instance : NotesDataBase? = null

        private val LOCK = Any()

        fun getInstance(context: Context): NotesDataBase{
            instance?.let{
                return it
            }
            synchronized(LOCK){
                instance?.let { return it }
            }
            return Room.databaseBuilder(
                context = context,
                klass = NotesDataBase::class.java,
                name = "notes.db"
            ).build().also { instance = it }
        }
    }
}