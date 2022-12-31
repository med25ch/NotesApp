package com.resse.notesapp.data.viewModels

import android.app.AlertDialog
import android.content.Context
import android.widget.Toast
import androidx.lifecycle.*
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.resse.notesapp.R
import com.resse.notesapp.data.adapters.ToDoListAdapter
import com.resse.notesapp.data.fragments.UpdateFragment
import com.resse.notesapp.data.models.ToDoData
import com.resse.notesapp.data.repository.ToDoRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import timber.log.Timber

class ToDoViewModel (private val repository: ToDoRepository) : ViewModel(){

    val dialogChoices = arrayOf("Priority : From newest","Priority : High to low", "Priority : Low to high","Creation date : From oldest")

    // Using LiveData and caching what allWords returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    val allToDoData: LiveData<List<ToDoData>> = repository.allToDoData.asLiveData()

    val getOldToNewAllToDoData: LiveData<List<ToDoData>> = repository.getOldToNewAllToDoData.asLiveData()

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun insert(toDoData: ToDoData) = viewModelScope.launch {
        repository.insert(toDoData)
    }

    fun updateData(toDoData: ToDoData) = viewModelScope.launch {
        repository.updateData(toDoData)
    }

    fun deleteData(toDoData: ToDoData) = viewModelScope.launch {
        repository.deleteData(toDoData)
    }

    fun deleteAllData() = viewModelScope.launch {
        repository.deleteAllData()
    }

    fun sortHighToLow() : LiveData<List<ToDoData>> {
        lateinit var result : LiveData<List<ToDoData>>
        viewModelScope.launch {
            var deffered = viewModelScope.async {
                repository.sortHighToLow()
            }
            result = deffered.await()
        }
        return result
    }

    fun sortLowToHigh() : LiveData<List<ToDoData>> {
        lateinit var result : LiveData<List<ToDoData>>
        viewModelScope.launch {
            var deffered = viewModelScope.async {
                repository.sortLowToHigh()
            }
            result = deffered.await()
        }
        return result
    }

    fun searchDatabase(searchQuery:String) : LiveData<List<ToDoData>>{

        lateinit var result : LiveData<List<ToDoData>>
        viewModelScope.launch {
             var deffered = viewModelScope.async {
                repository.searchDatabase(searchQuery)
            }
             result = deffered.await()
        }

        return result
    }
}

class ToDoViewModelFactory(private val repository: ToDoRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ToDoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ToDoViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}