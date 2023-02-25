package com.example.instagramv1.ui.mainscreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.instagramv1.data.repository.PostRepository
import com.example.instagramv1.data.repository.SampleRepository
import com.example.instagramv1.model.FilterOptions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(
    private val postRepository: PostRepository,
) : ViewModel() {

    var userId =-1

    val sortedOps = MutableLiveData<FilterOptions>(FilterOptions.SORT_BY_DATE_DESC)
    var sort = FilterOptions.SORT_BY_DATE_DESC
    var changed = MutableLiveData<Boolean>(true)

    val addReactions = mutableSetOf<Int>()
    val removeReactions = mutableSetOf<Int>()

    fun addAllReaction(){
        for(reaction in addReactions){
            addReaction(reaction)
        }
        addReactions.clear()
    }

    fun removeAllReaction(){
        for(reaction in removeReactions){
            removeReaction(reaction)
        }
        removeReactions.clear()
    }


    private fun addReaction(postId : Int){
        viewModelScope.launch {
            postRepository.addReaction(userId,postId)
        }
    }

    private fun removeReaction(postId : Int){
        viewModelScope.launch {
            postRepository.removeReaction(userId,postId)
        }
    }

    suspend fun getSavedStatus(postId : Int) : LiveData<Boolean>{
        return postRepository.getSavedStatus(userId,postId)
    }



    suspend fun getNumberOfReaction(postId: Int) : LiveData<Int>{
        return postRepository.getNumberOfReaction(postId)
    }

    suspend fun getNumberOfComments(postId: Int) : LiveData<Int>{
        return postRepository.getNumberOfComments(postId)
    }

    suspend fun isUserReacted(postId: Int) : LiveData<Boolean>{
        return postRepository.isUserReacted(userId,postId)
    }

    fun savePost(postId: Int){
        viewModelScope.launch {
            postRepository.savePost(userId,postId)
        }
    }

    fun unSavePost(postId: Int){
        viewModelScope.launch {
            postRepository.removeFromSavedPost(userId,postId)
        }
    }

    fun deletePost(postId: Int){
        viewModelScope.launch {
            postRepository.deletePost(postId)
        }
    }
}