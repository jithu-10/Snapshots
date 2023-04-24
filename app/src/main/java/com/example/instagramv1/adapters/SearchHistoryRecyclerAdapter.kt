package com.example.instagramv1.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.instagramv1.R
import com.example.instagramv1.databinding.CommentViewBinding
import com.example.instagramv1.databinding.SearchHistoryViewBinding
import com.example.instagramv1.ui.searchscreen.SearchViewModel

class SearchHistoryRecyclerAdapter(private val searchViewModel: SearchViewModel,private val eventListener : EventListener) : RecyclerView.Adapter<SearchHistoryRecyclerAdapter.SearchHistoryViewHolder>() {

    var historyList : MutableList<String> = mutableListOf()

    fun setHistory(list : List<String>){
        historyList.clear()
        historyList = list.toMutableList()
        notifyDataSetChanged()
    }

    inner class SearchHistoryViewHolder(private val searchHistoryViewBinding: SearchHistoryViewBinding) : RecyclerView.ViewHolder(searchHistoryViewBinding.root){

        fun bind(historyText : String){
            searchHistoryViewBinding.historyText = historyText

            searchHistoryViewBinding.searchHistoryLayout.setOnClickListener {
                eventListener.onSearchItemClickEvent(historyText)
            }
            searchHistoryViewBinding.searchHistoryTv.setOnClickListener {
                eventListener.onSearchItemClickEvent(historyText)
            }

            searchHistoryViewBinding.searchIv.setOnClickListener {
                eventListener.onSearchItemClickEvent(historyText)
            }

            searchHistoryViewBinding.deleteSearchBtn.setOnClickListener {
                searchViewModel.deleteSearchHistory(historyText)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchHistoryViewHolder {
        val searchHistoryViewBinding : SearchHistoryViewBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.search_history_view, parent, false)
        return SearchHistoryViewHolder(searchHistoryViewBinding)
    }

    override fun onBindViewHolder(holder: SearchHistoryViewHolder, position: Int) {
        val historyText = historyList[position]
        holder.bind(historyText)
    }

    override fun getItemCount(): Int {
        return historyList.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }


    interface EventListener{
        fun onSearchItemClickEvent(historyText : String)
    }
}