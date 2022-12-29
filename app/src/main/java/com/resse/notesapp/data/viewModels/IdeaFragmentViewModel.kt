package com.resse.notesapp.data.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.resse.notesapp.data.network.BoredActivity
import com.resse.notesapp.data.network.BoredApi
import kotlinx.coroutines.launch
import kotlin.random.Random

class IdeaFragmentViewModel : ViewModel(){

    // The internal MutableLiveData that stores the status of the most recent request
    private val _status = MutableLiveData<BoredActivity>()

    // The external immutable LiveData for the request status
    val status: LiveData<BoredActivity> = _status

    // List of Activity Types
    val activityTypes = mutableListOf<String>("education","social","diy","recreational","cooking","relaxation")


    /**
     * Call getBoredActivity() on init so we can display status immediately.
     */
//    init {
//        getBoredActivity("social")
//    }

    /**
     * Gets Bored activity information from the Bored API Retrofit service and updates the
     * [List] [LiveData].
     */
     fun getBoredActivity(checkedList: MutableList<String>) {

        var type = getActivityType(checkedList)

        viewModelScope.launch {
            try {
                val listResult = BoredApi.retrofitService.getActivity(type)
                _status.value = listResult

            }catch (e: Exception){
                val noData = BoredActivity("No Data From server - Reason : ${e.message} : ","No Data",0)
                _status.value = noData
            }
        }
    }

    private fun getActivityType(checkedList: MutableList<String>) =
        if (checkedList.isNotEmpty()) {
            val randomIndex = Random.nextInt(checkedList.size);
            checkedList[randomIndex]
        } else {
            val randomIndex = Random.nextInt(activityTypes.size);
            activityTypes[randomIndex]
        }
}