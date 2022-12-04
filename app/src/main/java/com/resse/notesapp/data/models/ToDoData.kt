package com.resse.notesapp.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "todo_table")
data class ToDoData (
    @PrimaryKey(autoGenerate = true)
    val id :  Int,
    var title: String,
    var priority: Priority,
    var description : String
    )