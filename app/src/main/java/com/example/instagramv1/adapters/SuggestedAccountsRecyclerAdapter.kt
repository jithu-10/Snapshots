package com.example.instagramv1.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.instagramv1.R

class SuggestedAccountsRecyclerAdapter : RecyclerView.Adapter<SuggestedAccountsRecyclerAdapter.SuggestedAccountsViewHolder>() {


    class SuggestedAccountsViewHolder(view : View) : ViewHolder(view){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SuggestedAccountsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.suggested_account_view,parent,false)
        return SuggestedAccountsViewHolder(view)
    }

    override fun onBindViewHolder(holder: SuggestedAccountsViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {
        return 20
    }
}