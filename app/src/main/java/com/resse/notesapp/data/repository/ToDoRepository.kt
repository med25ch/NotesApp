package com.resse.notesapp.data.repository

import androidx.annotation.WorkerThread
import com.resse.notesapp.data.ToDoDao
import com.resse.notesapp.data.models.ToDoData
import kotlinx.coroutines.flow.Flow

// Declares the DAO as a private property in the constructor. Pass in the DAO
// instead of the whole database, because you only need access to the DAO
class ToDoRepository(private val toDoDao: ToDoDao) {

        // Room executes all queries on a separate thread.
        // Observed Flow will notify the observer when the data has changed.
        val allToDoData: Flow<List<ToDoData>> = toDoDao.getAllToDoData()

        // By default Room runs suspend queries off the main thread, therefore, we don't need to
        // implement anything else to ensure we're not doing long running database work
        // off the main thread.
        @Suppress("RedundantSuspendModifier")
        @WorkerThread
        suspend fun insert(toDoData: ToDoData) {
            toDoDao.insertData(toDoData)
        }

}