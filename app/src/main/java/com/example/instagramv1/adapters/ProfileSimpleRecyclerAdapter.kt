package com.example.instagramv1.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.instagramv1.R

class ProfileSimpleRecyclerAdapter : RecyclerView.Adapter<ProfileSimpleRecyclerAdapter.ProfileViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.other_profile_simple_view,parent,false)
        return ProfileViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProfileViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {
        return 43
    }


    class ProfileViewHolder(view : View) : RecyclerView.ViewHolder(view) {

    }
}