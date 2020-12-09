package com.example.application.home.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.application.R
import com.example.application.home.models.RecyclerItem

/*
This Class connects the HomeFragment with the recycler items.
to make a personalized recylerview
This displayes the articles in the HomeFragment
 */

class RecyclerAdapter(
    private val itemList: List<RecyclerItem>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<RecyclerAdapter.RecyclerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.recycler_item, parent, false)
        return RecyclerViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        val currentItem = itemList[position]
        holder.title.text = currentItem.title
        holder.rating.text = currentItem.rating
        holder.description.text = currentItem.description
    }

    override fun getItemCount() = itemList.size

    inner class RecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        val title: TextView = itemView.findViewById(R.id.title)
        val rating: TextView = itemView.findViewById(R.id.rating)
        val description: TextView = itemView.findViewById(R.id.description)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position: Int = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(itemList[position])
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(item: RecyclerItem)
    }
}