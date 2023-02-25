package com.example.instagramv1.ui.mainscreen.profilescreen.connectionscreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.instagramv1.data.repository.UserRepository
import com.example.instagramv1.model.UserConnectionWithOtherUser
import com.example.instagramv1.model.UserMiniProfileData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConnectionViewModel @Inject constructor(
    val userRepository: UserRepository
) : ViewModel() {

    var userId = -1
    var followUsers = mutableSetOf<Int>()
    var unfollowUsers = mutableSetOf<Int>()

    fun followAllUsers(){
        for(user in followUsers){
            followUser(user)
        }
        followUsers.clear()
    }

    fun unFollowAllUsers(){
        for(user in unfollowUsers){
            unfollowUser(user)
        }
        unfollowUsers.clear()
    }


    suspend fun getFollowers() : LiveData<List<UserMiniProfileData>>{
        return userRepository.getUserFollowers(userId)
    }

    suspend fun getFollowings() : LiveData<List<UserMiniProfileData>>{
        return userRepository.getUserFollowing(userId)
    }

    suspend fun getFollowers(other_user_id : Int) : LiveData<List<UserMiniProfileData>>{
        return userRepository.getUserFollowers(other_user_id)
    }

    suspend fun getFollowings(other_user_id: Int) : LiveData<List<UserMiniProfileData>>{
        return userRepository.getUserFollowing(other_user_id)
    }

    fun getUserConnectionWithOtherUser(other_user_id: Int) : LiveData<UserConnectionWithOtherUser>{
        var userConnection : LiveData<UserConnectionWithOtherUser> = MutableLiveData()
        viewModelScope.launch {
            userConnection = userRepository.getUserConnectionWithOtherUser(userId,other_user_id)
        }
        return userConnection
    }

    suspend fun getNoOfFollowers(id : Int) : LiveData<Int>{
        return userRepository.getNoOfFollowers(id)
    }

    suspend fun getNoOfFollowings(id : Int) : LiveData<Int> {
        return userRepository.getNoOfFollowings(id)
    }



    fun removeFollower(followerUserId : Int) {
        viewModelScope.launch {
            userRepository.removeFollower(userId,followerUserId)
        }
    }

    fun unfollowUser(followedUserId : Int){
        viewModelScope.launch {
            userRepository.unfollowUser(followedUserId,userId)
        }
    }

    fun followUser(other_user_id: Int){
        viewModelScope.launch {
            userRepository.followUser(other_user_id,userId)
        }
    }

    fun cancelRequestUser(other_user_id: Int){
        viewModelScope.launch {
            userRepository.declineFollowRequest(other_user_id,userId)
        }
    }

}