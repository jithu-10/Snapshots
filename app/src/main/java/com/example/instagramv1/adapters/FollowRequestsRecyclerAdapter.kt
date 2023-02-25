package com.example.instagramv1.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.instagramv1.R

class FollowRequestsRecyclerAdapter : RecyclerView.Adapter<FollowRequestsRecyclerAdapter.FollowRequestsViewHolder>() {


    class FollowRequestsViewHolder(view : View) : RecyclerView.ViewHolder(view){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowRequestsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.follow_request_view,parent,false)
        return FollowRequestsViewHolder(view)
    }

    override fun onBindViewHolder(holder: FollowRequestsViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {
        return 4
    }
}