package com.example.instagramv1.ui.mainscreen.notificationscreen

import android.graphics.Bitmap
import android.util.Log
import android.widget.ListView
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.instagramv1.data.repository.PostRepository
import com.example.instagramv1.data.repository.UserRepository
import com.example.instagramv1.model.NotificationData
import com.example.instagramv1.model.NotificationType
import com.example.instagramv1.model.NotificationViewData
import com.example.instagramv1.model.UserMiniProfileData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    val userRepository: UserRepository,
    val postRepository: PostRepository
) : ViewModel() {

    var userId = -1
    val followUsers = mutableSetOf<Int>()


    var numberOfNotifications = 0


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

    suspend fun getNotificationUpdates() : LiveData<List<NotificationViewData>>{
        return userRepository.getNotificationUpdates(userId)
    }

    suspend fun getContentImage(contentId : Int, notificationViewType: NotificationType) : Bitmap{
        getPostId(contentId,notificationViewType)
        return if(notificationViewType == NotificationType.LIKED_YOUR_POST || notificationViewType == NotificationType.SAVED_YOUR_POST){
            postRepository.getPostImage(contentId)
        } else{
            val postId = postRepository.getPostFromComment(contentId)
            postRepository.getPostImage(postId)
        }

    }

    suspend fun getPostId(contentId: Int,notificationViewType: NotificationType) :Int   {
        return if(notificationViewType == NotificationType.LIKED_YOUR_POST || notificationViewType == NotificationType.SAVED_YOUR_POST){
            contentId
        } else{
            val postId = postRepository.getPostFromComment(contentId)
            postId
        }
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

    suspend fun getNotificationCount() : LiveData<Int>{
        return userRepository.getNotificationCount(userId)
    }

    fun clearNotificationCount(){
        viewModelScope.launch {
            userRepository.clearNotificationCount(userId)
        }

    }





}