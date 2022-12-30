package com.resse.notesapp.data.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.resse.notesapp.data.network.BoredActivity
import com.resse.notesapp.data.network.BoredApi
import com.resse.notesapp.data.uistate.UiState
import kotlinx.coroutines.launch
import kotlin.random.Random

class IdeaFragmentViewModel : BaseViewModel<UiState>(){

    // The internal MutableLiveData that stores the status of the most recent request
    private val _status = MutableLiveData<BoredActivity>()

    // The external immutable LiveData for the request status
    val status: LiveData<BoredActivity> = _status

    // List of Activity Types
    val activityTypes = mutableListOf("education","social","diy","recreational","cooking","relaxation")



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

        uiState.value = UiState.Loading

        viewModelScope.launch {
            try {
                val listResult = BoredApi.retrofitService.getActivity(type)
                uiState.value = UiState.Success
                _status.value = listResult

            }catch (e: Exception){
                val noData = BoredActivity("No Data From server - Reason : ${e.message} : ","No Data",0)
                uiState.value = UiState.Success
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