package com.example.instagramv1.ui.addpostscreen

import android.app.Activity
import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.instagramv1.data.repository.PostRepository
import com.example.instagramv1.data.repository.UserRepository
import com.example.instagramv1.model.PostData
import com.example.instagramv1.model.UserProfileData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AddPostViewModel @Inject constructor(
    private val postRepository: PostRepository
) : ViewModel() {


    var postImage : Bitmap? = null
    var orginalImage : Bitmap? = null

    var galleryImagePath : String? = null

    var location : String = ""
    var description : String = ""

    var userId  = -1
    var openCamera = -1




    suspend fun postImage(){
        description = description.trim()
        location = location.trim()
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