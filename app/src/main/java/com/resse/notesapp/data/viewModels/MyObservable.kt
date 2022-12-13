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

}