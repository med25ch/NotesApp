package com.resse.notesapp.data.room

import android.content.Context
import androidx.room.*
import com.resse.notesapp.data.models.ToDoData

@Database(entities = [ToDoData::class], version = 1, exportSchema = false)
@TypeConverters(Converter::class)
abstract class ToDoDataBase : RoomDatabase() {

    abstract fun toDoDao() : ToDoDao

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: ToDoDataBase? = null

        fun getDatabase(context: Context): ToDoDataBase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ToDoDataBase::class.java,
                    "todo_table"
                ).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }

}