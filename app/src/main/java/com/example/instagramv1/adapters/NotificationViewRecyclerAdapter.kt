package com.example.instagramv1.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.instagramv1.R

class NotificationViewRecyclerAdapter : RecyclerView.Adapter<NotificationViewRecyclerAdapter.NotificationViewViewHolder>() {


    class NotificationViewViewHolder(view : View) : RecyclerView.ViewHolder(view){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.notification_view,parent,false)
        return NotificationViewViewHolder(view)
    }

    override fun onBindViewHolder(holder: NotificationViewViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {
        return 3
    }
}