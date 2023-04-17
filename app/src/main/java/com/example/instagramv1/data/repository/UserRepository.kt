package com.example.instagramv1.data.repository

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import com.example.instagramv1.model.*

interface UserRepository {

    suspend fun getUserProfileData(userId : Int) : LiveData<UserProfileData>

    suspend fun getUserEditableData(userId : Int) : LiveData<UserEditableData>

    suspend fun getUserProfilePicture(userId: Int) : Bitmap?

    suspend fun getUserName(userId: Int) : String

    suspend fun updateUserData(userEditableData: UserEditableData,userId : Int)

    suspend fun isUserNameAlreadyExist(userName : String) : Boolean

    suspend fun isEmailAlreadyExist(email : String) : Boolean

    suspend fun isPhoneAlreadyExist(phone : String) : Boolean

    suspend fun changePrivacy(privateAccount : Boolean, userId: Int)

    suspend fun getSuggestedUsers(userId: Int) : LiveData<List<UserMiniProfileData>>

    suspend fun getUsers(name : String) : LiveData<List<UserMiniProfileData>>

    suspend fun getRequestedUsers(userId: Int) : LiveData<List<UserMiniProfileData>>

    suspend fun followUser(followed_user_id : Int, userId: Int)

    suspend fun unfollowUser(followed_user_id : Int, userId: Int)

    suspend fun getUserPrivacy(userId : Int) : LiveData<Boolean>

    suspend fun acceptFollowRequest(userId : Int, followerUserId : Int)

    suspend fun removeFollower(userId: Int,followerUserId: Int)

    suspend fun declineFollowRequest(userId : Int, followerUserId : Int)

    suspend fun getUserFollowers(userId :Int) : LiveData<List<UserMiniProfileData>>

    suspend fun getUserFollowing(userId :Int) : LiveData<List<UserMiniProfileData>>

    suspend fun getUserConnectionWithOtherUser(userId: Int, otherUserId : Int) : LiveData<UserConnectionWithOtherUser>

    suspend fun getNoOfFollowers(userId: Int) : LiveData<Int>

    suspend fun getNoOfFollowings(userId: Int) : LiveData<Int>

    suspend fun isPrivateAccount(userId: Int) : Boolean

    suspend fun getNotifications(userId: Int) : LiveData<List<NotificationData>>

    suspend fun getNotificationUpdates(userId: Int) : LiveData<List<NotificationViewData>>

    suspend fun removeProfilePicture(userId: Int)

    suspend fun getUserSearchHistory(userId: Int) : LiveData<List<String>>

    suspend fun addSearchHistory(userId: Int,historyText : String)

    suspend fun deleteSearchHistory(userId: Int,historyText : String)

    suspend fun deleteAllSearchHistory(userId: Int)

    suspend fun clearNotificationCount(userId: Int)

    suspend fun getNotificationCount(userId: Int) : LiveData<Int>



}