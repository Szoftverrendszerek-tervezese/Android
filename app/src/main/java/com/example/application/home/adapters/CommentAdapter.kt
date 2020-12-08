package com.example.application.home.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.application.R
import com.example.application.home.models.CommentItem


class CommentAdapter(private val itemList: List<CommentItem>) :
    RecyclerView.Adapter<CommentAdapter.CommentViewHolder>() {

    class CommentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userName : TextView = itemView.findViewById(R.id.userNameTextView)
        val date : TextView = itemView.findViewById(R.id.dateTextView)
        val text : TextView = itemView.findViewById(R.id.commentTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.comment_item, parent, false)
        return CommentViewHolder(itemView)
    }

    override fun getItemCount() = itemList.size

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val currentItem = itemList[position]
        holder.userName.text = currentItem.username
        holder.date.text = currentItem.timestamp
        holder.text.text = currentItem.commentText
    }
}