package com.resse.notesapp.data.adapters

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import androidx.databinding.BindingAdapter
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.resse.notesapp.R
import com.resse.notesapp.data.fragments.ListFragment
import com.resse.notesapp.data.interfaces.ItemClickListener
import com.resse.notesapp.data.models.Priority
import com.resse.notesapp.data.models.ToDoData
import com.resse.notesapp.data.viewModels.SharedViewModel
import com.resse.notesapp.data.viewModels.SharedViewModelFactory
import com.resse.notesapp.databinding.RowLayoutBinding

class ToDoListAdapter(private val itemClickListener: ItemClickListener) : ListAdapter<ToDoData, ToDoListAdapter.ToDoViewHolder>(ToDosComparator()){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoViewHolder {
        return ToDoViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: ToDoViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current)
        holder.itemView.setOnClickListener {
            itemClickListener.onItemClickListener(current)
        }
    }

    class ToDoViewHolder (private val binding: RowLayoutBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(toDoData: ToDoData) {

            binding.toDoData = toDoData
            binding.executePendingBindings()

            val color = ContextCompat.getColor(binding.root.context,getColorFromPriority(toDoData.priority))
            ImageViewCompat.setImageTintList(binding.ivPriorityIndicator, ColorStateList.valueOf(color))
        }

        private fun getColorFromPriority(priority: Priority): Int {

            return when(priority){
                Priority.LOW -> R.color.green_selected
                Priority.MEDIUM -> R.color.yellow_selected
                Priority.HIGH -> R.color.red_selected
            }
        }

        companion object {
            fun create(parent: ViewGroup): ToDoViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RowLayoutBinding.inflate(layoutInflater,parent,false)
                return ToDoViewHolder(binding)
            }
        }
    }


    class ToDosComparator : DiffUtil.ItemCallback<ToDoData>(){
        override fun areItemsTheSame(oldItem: ToDoData, newItem: ToDoData): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: ToDoData, newItem: ToDoData): Boolean {
            return oldItem.description == newItem.description
        }
    }
}



