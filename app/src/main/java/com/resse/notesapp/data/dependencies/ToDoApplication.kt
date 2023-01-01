package com.resse.notesapp.data.dependencies

import android.app.Application
import com.resse.notesapp.BuildConfig
import com.resse.notesapp.data.repository.ToDoRepository
import com.resse.notesapp.data.room.ToDoDataBase
import timber.log.Timber


class ToDoApplication : Application() {

    // Using by lazy so the database and the repository are only created when they're needed
    // rather than when the application starts
    val database by lazy { ToDoDataBase.getDatabase(this) }
    val repository by lazy { ToDoRepository(database.toDoDao()) }


    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}