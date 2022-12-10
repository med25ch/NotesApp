package com.resse.notesapp.data.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.resse.notesapp.R
import com.resse.notesapp.data.models.ToDoData

class ToDoListAdapter : ListAdapter<ToDoData, ToDoListAdapter.ToDoViewHolder>(ToDosComparator()){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoViewHolder {
        return ToDoViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: ToDoViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current.title,current.description)
    }

    class ToDoViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView){

        private val titleTxt:TextView = itemView.findViewById(R.id.title_txt)
        private val descriptionTxt:TextView = itemView.findViewById(R.id.description_txt)

        fun bind(title: String?,description : String?) {
            titleTxt.text = title
            descriptionTxt.text = description
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

