package com.resse.notesapp.data.adapters

import android.app.Application
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.resse.notesapp.R
import com.resse.notesapp.data.models.Priority
import com.resse.notesapp.data.models.ToDoData
import timber.log.Timber

class ToDoListAdapter : ListAdapter<ToDoData, ToDoListAdapter.ToDoViewHolder>(ToDosComparator()){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoViewHolder {
        return ToDoViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: ToDoViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current.title,current.description,current.priority)
    }

    class ToDoViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView){

        private val titleTxt:TextView = itemView.findViewById(R.id.title_txt)
        private val descriptionTxt:TextView = itemView.findViewById(R.id.description_txt)
        private val priorityImage : ImageView = itemView.findViewById(R.id.iv_priority_indicator)

        fun bind(title: String?, description: String?, priority: Priority) {
            titleTxt.text = title
            descriptionTxt.text = description
            val color = ContextCompat.getColor(itemView.context,getColorFromPriority(priority))
            ImageViewCompat.setImageTintList(priorityImage, ColorStateList.valueOf(color))
        }

        private fun getColorFromPriority(priority: Priority): Int {

            return when(priority){
                Priority.LOW -> R.color.green_selected
                Priority.MEDIUM -> R.color.yellow_selected
                Priority.HIGH -> R.color.red_selected
                else -> {
                    R.color.yellow_selected
                }
            }
        }

        companion object {
            fun create(parent: ViewGroup): ToDoViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.row_layout, parent, false)
                return ToDoViewHolder(view)
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



