package com.resse.notesapp.data.viewModels

import android.app.AlertDialog
import android.app.Application
import android.content.Context
import android.text.TextUtils
import android.widget.Toast
import androidx.lifecycle.*
import androidx.navigation.fragment.findNavController
import com.resse.notesapp.R
import com.resse.notesapp.data.fragments.UpdateFragment
import com.resse.notesapp.data.models.Priority
import com.resse.notesapp.data.models.ToDoData
import timber.log.Timber

class SharedViewModel(val app: Application) : ViewModel() {

    val emptyDatabase : MutableLiveData<Boolean> = MutableLiveData(true)

    fun isDatabaseEmpty(toDoData: List<ToDoData>){
        emptyDatabase.value = toDoData.isEmpty()
    }

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

    //Show AlertDialog to confirm deleting a note
    fun confirmItemRemoval(context: Context,mTodoViewModel:ToDoViewModel,toDoData: ToDoData,fragment: UpdateFragment){
        val builder = AlertDialog.Builder(context)
        var success = false
        builder.setPositiveButton(context.getString(R.string.POSITIVE_BUTTON_TEXT)){ _, _ ->
            mTodoViewModel.deleteData(toDoData)
            Toast.makeText(
                context,
                context.getString(R.string.SUCCESS_REMOVE_ITEM),
                Toast.LENGTH_SHORT
            ).show()

            fragment.findNavController().navigate(R.id.action_updateFragment_to_listFragment)
            success = true
            Timber.d("Remove item id : ${toDoData.id} : Operation succeeded : $success")

        }
        builder.setNegativeButton(context.getString(R.string.NEGATIVE_BUTTON_TEXT)){_,_ ->
            Timber.d("Remove item id : ${toDoData.id} : Operation Canceled by user")
        }
        builder.setTitle(context.getString(R.string.REMOVE_DIALOG_ITEM))
        builder.setMessage(context.getString(R.string.REMOVE_CONFIRMATION_MESSAGE))
        builder.create().show()
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