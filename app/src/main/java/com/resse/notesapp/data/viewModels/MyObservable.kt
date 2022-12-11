package com.resse.notesapp.data.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.resse.notesapp.data.models.ToDoData

class MyObservable : ViewModel(){
    val data = MutableLiveData<ToDoData>()

    fun data(item : ToDoData){
        data.value = item
    }

}