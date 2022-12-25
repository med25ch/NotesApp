package com.resse.notesapp.data.viewModels

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.*
import androidx.savedstate.SavedStateRegistryOwner

class SavedStateViewModel (private val state : SavedStateHandle) : ViewModel(){

    val SORTING_KEY = "SORTING_INDEX"

    fun saveSortingIndex(index : Int){
        state[SORTING_KEY] = index
    }

    fun restoreSortingIndex() : Int?{
       return state.get<Int>(SORTING_KEY)
    }

    fun getAllKeys(): Set<String> {
        return state.keys()
    }
}

//class SavedStateViewModelFactory(owner: SavedStateRegistryOwner,defaultArgs : Bundle?) : AbstractSavedStateViewModelFactory(owner,defaultArgs) {
//    override fun <T : ViewModel?> create(
//        key: String,
//        modelClass: Class<T>,
//        handle: SavedStateHandle
//    ): T = SavedStateViewModel(handle) as T
//
//}