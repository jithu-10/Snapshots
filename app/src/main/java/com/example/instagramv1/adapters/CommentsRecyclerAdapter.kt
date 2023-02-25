package com.example.instagramv1.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.instagramv1.R

class CommentsRecyclerAdapter : RecyclerView.Adapter<CommentsRecyclerAdapter.CommentsViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.comment_view,parent,false)
        return CommentsViewHolder(view)
    }

    override fun onBindViewHolder(holder: CommentsViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {
        return 3
    }


    class CommentsViewHolder(view : View) : RecyclerView.ViewHolder(view) {

    }
}