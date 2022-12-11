package com.resse.notesapp.data.viewModels

import android.app.Application
import android.text.TextUtils
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.resse.notesapp.data.dependencies.ToDoApplication
import com.resse.notesapp.data.models.Priority
import com.resse.notesapp.data.models.ToDoData
import com.resse.notesapp.data.repository.ToDoRepository

class SharedViewModel(val app: Application) : ViewModel() {

    fun verifyDataFromUser(mTitle: String, mDescription: String): Boolean {
        return if (TextUtils.isEmpty(mTitle) || TextUtils.isEmpty(mDescription)) {
            false
        } else !(mTitle.isEmpty() || mDescription.isEmpty())
    }

    fun parsePriority(priority: String) : Priority {
        return when(priority){
            "LOW" -> {
                Priority.LOW}
            "MEDIUM" -> {
                Priority.MEDIUM}
            "HIGH" -> {
                Priority.HIGH}
            else -> {
                Priority.LOW}
        }
    }
}

class SharedViewModelFactory(val app: Application?) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SharedViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return app?.let { SharedViewModel(it) } as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}