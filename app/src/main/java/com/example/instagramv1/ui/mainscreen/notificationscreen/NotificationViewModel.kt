package com.example.instagramv1.ui.mainscreen.notificationscreen

import android.util.Log
import android.widget.ListView
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.instagramv1.data.repository.UserRepository
import com.example.instagramv1.model.NotificationData
import com.example.instagramv1.model.UserMiniProfileData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    val userRepository: UserRepository
) : ViewModel() {

    var userId = -1
    val followUsers = mutableSetOf<Int>()


    fun followAllUsers(){
        for(user in followUsers){
            followUser(user)
        }
        followUsers.clear()
    }


    suspend fun getSuggestedUsers() : LiveData<List<UserMiniProfileData>>{
        return userRepository.getSuggestedUsers(userId)
    }

    suspend fun getFollowRequests() : LiveData<List<UserMiniProfileData>>{
        return userRepository.getRequestedUsers(userId)
    }

    suspend fun getNotifications() : LiveData<List<NotificationData>>{
        return userRepository.getNotifications(userId)
    }

    fun acceptFollowRequest(followerUserId : Int){
        viewModelScope.launch {
            userRepository.acceptFollowRequest(userId,followerUserId)
        }
    }

    fun deleteFollowRequest(followerUserId: Int){
        viewModelScope.launch {
            userRepository.declineFollowRequest(userId,followerUserId)
        }
    }


    fun followUser(followedUserId : Int){
        Log.d("Follow User","Before Scope")
        viewModelScope.launch {
            userRepository.followUser(followedUserId,userId)
            Log.d("Follow User","Follow User")
        }
    }





}