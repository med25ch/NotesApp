package com.resse.notesapp.data.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.resse.notesapp.data.network.BoredActivity
import com.resse.notesapp.data.network.BoredApi
import kotlinx.coroutines.launch

class IdeaFragmentViewModel : ViewModel(){

    // The internal MutableLiveData that stores the status of the most recent request
    private val _status = MutableLiveData<BoredActivity>()

    // The external immutable LiveData for the request status
    val status: LiveData<BoredActivity> = _status

    /**
     * Call getBoredActivity() on init so we can display status immediately.
     */
    init {
        getBoredActivity()
    }

    /**
     * Gets Bored activity information from the Bored API Retrofit service and updates the
     * [List] [LiveData].
     */
    private fun getBoredActivity() {

        viewModelScope.launch {
            try {
                val listResult = BoredApi.retrofitService.getActivity()
                _status.value = listResult

            }catch (e: Exception){
                val noData = BoredActivity("No Data From server - Reason : ${e.message} : ","No Data",0)
                _status.value = noData
            }
        }
    }
}