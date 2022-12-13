package com.resse.notesapp.data.viewModels

import android.widget.RadioButton
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.resse.notesapp.data.models.Priority
import com.resse.notesapp.data.models.ToDoData

class MyObservable : ViewModel() {
    val data = MutableLiveData<ToDoData>()

    fun data(item: ToDoData) {
        data.value = item
    }

    fun validatePriority(priority: Priority, radioButtonList: MutableList<RadioButton>) {
        when (priority) {
            Priority.HIGH -> {
                radioButtonList[0].isChecked = true
            }
            Priority.MEDIUM-> {
                radioButtonList[1].isChecked = true
            }
            Priority.LOW -> {
                radioButtonList[2].isChecked = true
            }
        }
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

    fun updateToDoData(id:Int,title:String,description:String,priority:String) : ToDoData{
        val priority = parsePriority(priority)
        return ToDoData(id,title,priority,description)
    }

}