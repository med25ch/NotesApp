package com.resse.notesapp.data.viewModels

import android.app.AlertDialog
import android.content.Context
import android.widget.Toast
import androidx.lifecycle.*
import androidx.navigation.fragment.findNavController
import com.resse.notesapp.R
import com.resse.notesapp.data.fragments.UpdateFragment
import com.resse.notesapp.data.models.ToDoData
import com.resse.notesapp.data.repository.ToDoRepository
import kotlinx.coroutines.launch
import timber.log.Timber

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

    fun deleteAllData() = viewModelScope.launch {
        repository.deleteAllData()
    }

    fun confirmItemRemoval(context: Context){
        val builder = AlertDialog.Builder(context)

        builder.setPositiveButton(context.getString(R.string.POSITIVE_BUTTON_TEXT)){ _, _ ->
            //Remove all notes
            deleteAllData()
            Toast.makeText(
                context,
                context.getString(R.string.SUCCESS_REMOVE_ITEM),
                Toast.LENGTH_SHORT
            ).show()

            Timber.d("Remove all items : Operation succeeded ")

        }
        builder.setNegativeButton(context.getString(R.string.NEGATIVE_BUTTON_TEXT)){ _, _ ->
            Timber.d("Remove all items : Operation Canceled by user")
        }
        builder.setTitle(context.getString(R.string.REMOVE_ITEMS_DIALOG))
        builder.setMessage(context.getString(R.string.REMOVE_CONFIRMATION_MESSAGE))
        builder.create().show()
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