package com.example.instagramv1.ui.addpostscreen

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.instagramv1.data.repository.PostRepository
import com.example.instagramv1.model.PostViewData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EditPostViewModel @Inject constructor(
    private val postRepository: PostRepository
) : ViewModel(){

    var location : String? = null
    var description : String? = null
    var postImage : Bitmap? = null
    var userId = -1
    var postId = -1


    suspend fun updatePostDetails(){
        Log.d("Edit Option","New Location : $location")
        Log.d("Edit Option","New Description : $description")
        postRepository.changePostLocation(postId,location)
        postRepository.changePostDescription(postId,description)
    }

    suspend fun getPost() : LiveData<PostViewData> {
        return postRepository.getPost(postId,userId)
    }
}