package com.resse.notesapp.data.viewModels

import androidx.lifecycle.*
import com.resse.notesapp.data.models.ToDoData
import com.resse.notesapp.data.repository.ToDoRepository
import kotlinx.coroutines.launch

class ToDoViewModel (private val repository: ToDoRepository) : ViewModel(){
    // Using LiveData and caching what allWords returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    val allToDoData: LiveData<List<ToDoData>> = repository.allToDoData.asLiveData()

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