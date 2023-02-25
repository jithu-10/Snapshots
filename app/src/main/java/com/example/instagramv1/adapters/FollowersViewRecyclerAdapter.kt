package com.example.instagramv1.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.instagramv1.R

class FollowersViewRecyclerAdapter : RecyclerView.Adapter<FollowersViewRecyclerAdapter.FollowersViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowersViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.followers_view,parent,false)
        return FollowersViewHolder(view)
    }

    override fun onBindViewHolder(holder: FollowersViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {
        return 43
    }


    class FollowersViewHolder(view : View) : RecyclerView.ViewHolder(view) {

    }
}