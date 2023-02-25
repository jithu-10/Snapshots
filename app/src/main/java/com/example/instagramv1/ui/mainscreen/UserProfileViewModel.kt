package com.example.instagramv1.ui.mainscreen

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.instagramv1.data.repository.UserRepository
import com.example.instagramv1.model.UserProfileData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
) : ViewModel() {

    var userId = -1

    private var _userProfileData : MutableLiveData<UserProfileData> = MutableLiveData<UserProfileData>()
    val userProfileData : LiveData<UserProfileData> = _userProfileData

    var postImage : Bitmap? = null


    var profilePicture : Bitmap? = null
    var fullName : String? = null
    var userName : String? = null
    var noOfPosts : Int? = null
    var noOfFollowers : Int? = null
    var noOfFollowing : Int? = null
    var privateAccount : Boolean? = null






    suspend fun getUserProfileData() : LiveData<UserProfileData>{
        return userRepository.getUserProfileData(userId)
    }

    suspend fun getUserPrivacy() : LiveData<Boolean>{
        return userRepository.getUserPrivacy(userId)
    }

    fun changePrivacy(privateAccount : Boolean){
        viewModelScope.launch {
            userRepository.changePrivacy(privateAccount,userId)
        }
    }

}