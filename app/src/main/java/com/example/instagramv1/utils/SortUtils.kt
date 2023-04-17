package com.example.instagramv1.utils

import com.example.instagramv1.model.PostViewData

object SortUtils {

    fun sortByMostComments(list : List<PostViewData>) : List<PostViewData>{
        val newList = list.sortedByDescending {
            it.post_comments_count
        }
        return newList
    }

    fun sortByLikes(list : List<PostViewData>) : List<PostViewData>{
        val newList = list.sortedByDescending {
            it.post_reaction_count
        }
        return newList
    }

    fun sortByDateAscending(list : List<PostViewData>) : List<PostViewData>{
        val newList = list.sortedBy {
            it.post_created_time
        }
        return newList
    }

    fun sortByDateDescending(list : List<PostViewData>) : List<PostViewData>{
        val newList = list.sortedByDescending {
            it.post_created_time
        }
        return newList
    }
}