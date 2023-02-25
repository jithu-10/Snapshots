package com.example.instagramv1.ui.addpostscreen

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.instagramv1.data.repository.PostRepository
import com.example.instagramv1.data.repository.UserRepository
import com.example.instagramv1.model.PostData
import com.example.instagramv1.model.UserProfileData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddPostViewModel @Inject constructor(
    private val postRepository: PostRepository
) : ViewModel() {


    var postImage : Bitmap? = null
    var orginalImage : Bitmap? = null

    var location : String = ""
    var description : String = ""

    var userId  = -1
    var openCamera = -1



    suspend fun postImage(){
        val postData =
            PostData(
                userId,
                postImage!!,
                location.ifBlank { null },
                description.ifBlank { null }
            )

        postRepository.postImage(postData)
    }





}