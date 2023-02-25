package com.example.instagramv1.ui.commentscreen

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.instagramv1.data.repository.PostRepository
import com.example.instagramv1.data.repository.UserRepository
import com.example.instagramv1.model.CommentData
import com.example.instagramv1.model.CommentViewData
import com.example.instagramv1.model.PostViewData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CommentViewModel @Inject constructor(
    private val postRepository: PostRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    var userId = -1
    var userImage : Bitmap? = null
    var userName : String = ""

    var postId = -1

    var postOwnerUserName = ""
    var postOwnerUserId = -1
    var postOwnerProfilePicture : Bitmap? = null
    var postImage : Bitmap? = null
    var postDescription : String? = null



    var commentText : String = ""

    val addCommentReactions = mutableSetOf<Int>()
    val removeCommentReactions = mutableSetOf<Int>()
    val deleteComments = mutableSetOf<Int>()
    val selectedComments = mutableSetOf<Int>()

    fun addAllCommentReaction(){
        for(reaction in addCommentReactions){
            addCommentReaction(reaction)
        }
        addCommentReactions.clear()
    }

    fun removeAllCommentReaction(){
        for(reaction in removeCommentReactions){
            removeCommentReaction(reaction)
        }
        removeCommentReactions.clear()
    }

    fun deleteAllComments(){
        for(comment in deleteComments){
            deleteComment(comment)
        }
        deleteComments.clear()
    }

    suspend fun getComments() : LiveData<List<CommentViewData>>{
        return postRepository.getPostComments(postId,userId)
    }

    fun postComment(comment : String) {
        val commentData = CommentData(userId,postId,comment)
        viewModelScope.launch {
            postRepository.postComment(commentData)
        }

    }

    suspend fun getPost() : LiveData<PostViewData>{
        return postRepository.getPost(postId,userId)
    }



    suspend fun getUserProfilePicture() : Bitmap?{
        return userRepository.getUserProfilePicture(userId)
    }

    suspend fun getUserName() : String{
        return userRepository.getUserName(userId)
    }

    private fun addCommentReaction(commentId : Int){
        viewModelScope.launch {
            postRepository.addCommentReaction(userId,commentId)
        }
    }

    private fun removeCommentReaction(commentId : Int){
        viewModelScope.launch {
            postRepository.removeCommentReaction(userId,commentId)
        }
    }

    private fun deleteComment(commentId : Int){
        viewModelScope.launch {
            postRepository.deleteComment(commentId)
        }
    }
}