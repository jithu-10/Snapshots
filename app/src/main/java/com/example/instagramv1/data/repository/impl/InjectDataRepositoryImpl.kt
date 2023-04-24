package com.example.instagramv1.data.repository.impl

import android.graphics.Bitmap
import android.util.Log
import com.example.instagramv1.data.dao.AccountDao
import com.example.instagramv1.data.dao.NotificationDao
import com.example.instagramv1.data.dao.PostDao
import com.example.instagramv1.data.dao.UserDao
import com.example.instagramv1.data.entities.Account
import com.example.instagramv1.data.entities.NotificationCount
import com.example.instagramv1.data.entities.Post
import com.example.instagramv1.data.entities.User
import com.example.instagramv1.data.repository.InjectDataRepository
import java.util.*

class InjectDataRepositoryImpl(
    private val accountDao : AccountDao,
    private val userDao : UserDao,
    private val postDao : PostDao,
    private val notificationDao: NotificationDao
) : InjectDataRepository {

    override suspend fun addAccount(
        email: String,
        phone: String,
        password: String,
        createdDate: Date,
        userName: String,
        fullName: String,
        profilePicture: Bitmap?,
        privateAccount: Boolean,
        userId: Int,
        accountId : Int
    ) {
        addUser(userName,fullName,profilePicture,privateAccount,userId)
        notificationDao.insertNotificationCount(NotificationCount(userId,0))
        accountDao.insertAccount(Account(email,phone,password,createdDate,userName,userId,accountId))

    }

    override suspend fun addUser(
        userName: String,
        fullName: String,
        profilePicture: Bitmap?,
        privateAccount: Boolean,
        userId: Int
    ) : Long{
        return userDao.insertUser(User(userName,fullName,profilePicture,privateAccount,userId))
    }

    override suspend fun addPost(
        userId: Int,
        createdDate: Date,
        image: Bitmap,
        description: String?,
        location: String?,
        postId : Int
    ) {
        Log.d("Injecting", "$userId $createdDate $image $description $location $postId");
        postDao.insertPost(Post(userId,createdDate,image,description,location,postId))
    }

    override suspend fun addConnection(following_user_id: Int, followed_user_id: Int) {

    }

    override suspend fun addComment(userId: Int, postId: Int, commentedTime: Date, description: String) {

    }
}