package com.resse.notesapp.data.repository

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.resse.notesapp.data.models.ToDoData
import com.resse.notesapp.data.room.ToDoDao
import kotlinx.coroutines.flow.Flow

// Declares the DAO as a private property in the constructor. Pass in the DAO
// instead of the whole database, because you only need access to the DAO
class ToDoRepository(private val toDoDao: ToDoDao) {

        // Room executes all queries on a separate thread.
        // Observed Flow will notify the observer when the data has changed.

        //Newest to Oldest data
        val allToDoData: Flow<List<ToDoData>> = toDoDao.getAllToDoData()

        //Oldest to newest Data
        val getOldToNewAllToDoData : Flow<List<ToDoData>> = toDoDao.getAllToDoData()

        // By default Room runs suspend queries off the main thread, therefore, we don't need to
        // implement anything else to ensure we're not doing long running database work
        // off the main thread.
        @Suppress("RedundantSuspendModifier")
        @WorkerThread
        suspend fun insert(toDoData: ToDoData) {
            toDoDao.insertData(toDoData)
        }

        @Suppress("RedundantSuspendModifier")
        @WorkerThread
        suspend fun updateData(toDoData: ToDoData){
                toDoDao.updateData(toDoData)
        }

        @Suppress("RedundantSuspendModifier")
        @WorkerThread
        suspend fun deleteData(toDoData: ToDoData){
                toDoDao.deleteData(toDoData)
        }

        @Suppress("RedundantSuspendModifier")
        @WorkerThread
        suspend fun deleteAllData(){
                toDoDao.deleteAllData()
        }

        @Suppress("RedundantSuspendModifier")
        @WorkerThread
        suspend fun searchDatabase(searchQuery: String) : LiveData<List<ToDoData>> {
                return toDoDao.searchDatabase(searchQuery)
        }

        @Suppress("RedundantSuspendModifier")
        @WorkerThread
        suspend fun sortHighToLow() : LiveData<List<ToDoData>> {
                return toDoDao.sortHighToLow()
        }

        @Suppress("RedundantSuspendModifier")
        @WorkerThread
        suspend fun sortLowToHigh() : LiveData<List<ToDoData>> {
                return toDoDao.sortLowToHigh()
        }
}