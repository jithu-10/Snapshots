package com.example.instagramv1.ui.mainscreen.othersprofilescreen

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.instagramv1.data.repository.UserRepository
import com.example.instagramv1.model.UserConnectionWithOtherUser
import com.example.instagramv1.model.UserProfileData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OthersProfileViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel(){

    var userId : Int = -1
    var otherUserId : Int = -1

    var profilePicture : Bitmap? = null
    var fullName : String? = null
    var userName : String? = null
    var noOfPosts : Int? = null
    var noOfFollowers : Int? = null
    var noOfFollowing : Int? = null
    var privateAccount : Boolean? = null

    var followUser = false
    var unfollowUser = false

    var userConnectionWithOtherUser : UserConnectionWithOtherUser? = null

    suspend fun getOtherUserProfileData() : LiveData<UserProfileData>{
        return userRepository.getUserProfileData(otherUserId)
    }

    suspend fun getUserConnectionWithOtherUser() : LiveData<UserConnectionWithOtherUser>{
        return userRepository.getUserConnectionWithOtherUser(userId,otherUserId)
    }

    fun followUser(){
        viewModelScope.launch {
            userRepository.followUser(otherUserId,userId)
        }
    }

    fun unFollowUser(){
        viewModelScope.launch {
            userRepository.unfollowUser(otherUserId,userId)
        }
    }

    fun cancelRequestUser(){
        viewModelScope.launch {
            userRepository.declineFollowRequest(otherUserId,userId)
        }
    }

    suspend fun isPrivateAccount() : Boolean{
        return userRepository.isPrivateAccount(otherUserId)
    }



}