package com.resse.notesapp.data.adapters

import android.view.View
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.navigation.findNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.resse.notesapp.R

class BindingAdapters {

    companion object{

        @BindingAdapter("android:navigateToAddFragment")
        @JvmStatic
        fun navigateToAddFragment(view:FloatingActionButton,navigate:Boolean){
            view.setOnClickListener{
                if (navigate){
                    view.findNavController().navigate(R.id.action_listFragment_to_addFragment)
                }
            }
        }

        @BindingAdapter("android:navigateToIdeaFragment")
        @JvmStatic
        fun navigateToIdeaFragment(view:FloatingActionButton,navigate:Boolean){
            view.setOnClickListener{
                if (navigate){
                    view.findNavController().navigate(R.id.action_listFragment_to_ideaFragment)
                }
            }
        }

        // Show empty Recyclerview
        @BindingAdapter("android:emptyDatabase")
        @JvmStatic
        fun emptyDatabase(view: View,emptyDatabase:MutableLiveData<Boolean>){
            when(emptyDatabase.value){
                true -> view.visibility = View.VISIBLE
                false -> view.visibility = View.INVISIBLE
                else -> {}
            }
        }

//        @BindingAdapter("android:getColorFromPriority")
//        @JvmStatic
//        fun getColorFromPriority()
    }
}