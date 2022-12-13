package com.resse.notesapp.data.room

import androidx.room.*
import com.resse.notesapp.data.models.ToDoData
import kotlinx.coroutines.flow.Flow

@Dao
interface ToDoDao {

    @Query("SELECT * FROM todo_table ORDER BY id ASC")
    fun getAllToDoData() : Flow<List<ToDoData>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertData(toDoData: ToDoData)
    @Update
    suspend fun updateData(toDoData: ToDoData)
}