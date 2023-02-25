package com.example.instagramv1.data.repository.impl

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.instagramv1.data.dao.*
import com.example.instagramv1.data.entities.Connection
import com.example.instagramv1.data.entities.ConnectionRequest
import com.example.instagramv1.data.entities.Notification
import com.example.instagramv1.data.entities.SearchHistory
import com.example.instagramv1.data.repository.UserRepository
import com.example.instagramv1.model.*
import java.util.*

class UserRepositoryImpl(
    private val userDao: UserDao,
    private val accountDao : AccountDao,
    private val connectionDao: ConnectionDao,
    private val notificationDao : NotificationDao
) : UserRepository
{

    override suspend fun getUserProfileData(userId: Int): LiveData<UserProfileData> {
        return userDao.getUserProfileData(userId)
    }

    override suspend fun getUserEditableData(userId : Int) : LiveData<UserEditableData>{
        return userDao.getUserEditableData(userId)
    }

    override suspend fun getUserProfilePicture(userId: Int): Bitmap? {
        return userDao.getUserProfilePicture(userId)
    }

    override suspend fun getUserName(userId: Int): String {
        return userDao.getUserName(userId)
    }

    override suspend fun isUserNameAlreadyExist(userName: String): Boolean {

        return accountDao.isUserNameExist(userName)
    }

    override suspend fun isEmailAlreadyExist(email: String): Boolean {
        return accountDao.isEmailExist(email)
    }

    override suspend fun isPhoneAlreadyExist(phone: String): Boolean {
        return accountDao.isPhoneExist(phone)
    }

    override suspend fun changePrivacy(privateAccount: Boolean, userId: Int) {
        userDao.updateAccountPrivacy(privateAccount, userId)
        if(!privateAccount){
            val listOfRequests : List<ConnectionRequest> = userDao.getRequestedUsersList(userId)
            for(request in listOfRequests){
                userDao.addConnection(Connection(request.followingUserId,request.followedUserId))
                connectionDao.removeConnectionRequest(request.followingUserId,request.followedUserId)
            }
        }
    }

    override suspend fun getSuggestedUsers(userId: Int) : LiveData<List<UserMiniProfileData>> {
        return userDao.getSuggestedUsers(userId)
    }

    override suspend fun getUsers(name: String): LiveData<List<UserMiniProfileData>> {
        val _name = name.trim().lowercase() + "%"
        return userDao.getUsers(_name)
    }

    override suspend fun getRequestedUsers(userId: Int): LiveData<List<UserMiniProfileData>> {
        return userDao.getRequestedUsers(userId)
    }

    override suspend fun followUser(followed_user_id: Int, userId: Int) {
        val privateAccount = userDao.isPrivateAccount(followed_user_id)
        if(privateAccount){
            connectionDao.requestConnection(ConnectionRequest(userId,followed_user_id))
        }
        else{
            if(!connectionDao.isConnected(userId,followed_user_id)){
                connectionDao.addConnection(Connection(userId,followed_user_id))
                notificationDao.addNotification(Notification(userId,followed_user_id,"started following you", Date()))
            }

        }

    }

    override suspend fun unfollowUser(followed_user_id: Int, userId: Int) {
        connectionDao.removeConnection(userId,followed_user_id)
        notificationDao.addNotification(Notification(userId,followed_user_id,"unfollows you", Date()))
    }

    override suspend fun getUserPrivacy(userId: Int) : LiveData<Boolean> {
        return userDao.isPrivateAccountLive(userId)
    }

    override suspend fun acceptFollowRequest(userId: Int, followerUserId: Int) {
        connectionDao.addConnection(Connection(followerUserId,userId))
        notificationDao.addNotification(Notification(followerUserId,userId,"started following you", Date()))
        connectionDao.removeConnectionRequest(followerUserId,userId)

    }

    override suspend fun removeFollower(userId: Int, followerUserId: Int) {
        connectionDao.removeConnection(followerUserId,userId)
    }

    override suspend fun declineFollowRequest(userId: Int, followerUserId: Int) {
        connectionDao.removeConnectionRequest(followerUserId,userId)
    }

    override suspend fun getUserFollowers(userId: Int): LiveData<List<UserMiniProfileData>> {
        return userDao.getUserFollowers(userId)
    }

    override suspend fun getUserFollowing(userId: Int): LiveData<List<UserMiniProfileData>> {
        return userDao.getUserFollowings(userId)
    }

    override suspend fun getUserConnectionWithOtherUser(userId: Int, otherUserId: Int)
    : LiveData<UserConnectionWithOtherUser> {
        return Transformations.map(connectionDao.getUserConnectionWithOtherUsers(userId,otherUserId)){
            when(it){
                1 -> UserConnectionWithOtherUser.FOLLOWING
                2 -> UserConnectionWithOtherUser.REQUESTED_TO_FOLLOW
                else -> UserConnectionWithOtherUser.NOT_FOLLOWED
            }
        }
    }

    override suspend fun getNoOfFollowers(userId: Int) : LiveData<Int>{
        return userDao.getNoOfFollowers(userId)
    }

    override suspend fun getNoOfFollowings(userId: Int) : LiveData<Int>{
        return userDao.getNoOfFollowings(userId)
    }

    override suspend fun isPrivateAccount(userId: Int): Boolean {
        return  userDao.isPrivateAccount(userId)
    }

    override suspend fun getNotifications(userId: Int): LiveData<List<NotificationData>> {
        return userDao.getNotifications(userId)
    }

    override suspend fun removeProfilePicture(userId: Int) {
        userDao.removeProfilePicture(null,userId)
    }

    override suspend fun getUserSearchHistory(userId: Int) : LiveData<List<String>>{
        return userDao.getSearchHistory(userId)
    }

    override suspend fun addSearchHistory(userId: Int, historyText: String) {
        if(userDao.containsSearchText(historyText,userId)){
            userDao.deleteSearchHistory(userId,historyText)
        }
        userDao.addSearchHistory(SearchHistory(historyText,userId))
    }

    override suspend fun deleteSearchHistory(userId: Int, historyText: String) {
        userDao.deleteSearchHistory(userId,historyText)
    }

    override suspend fun deleteAllSearchHistory(userId: Int) {
        userDao.deleteAllSearchHistory(userId)
    }


    override suspend fun updateUserData(userEditableData: UserEditableData,userId: Int) {
        userDao.updateUserName(userEditableData.user_name,userId)
        accountDao.updateUserName(userEditableData.user_name,userId)
        userDao.updateFullName(userEditableData.full_name,userId)
        userDao.updateProfilePicture(userEditableData.profile_picture,userId)
        accountDao.updateEmail(userEditableData.email,userId)
        accountDao.updatePhone(userEditableData.phone,userId)



    }
}