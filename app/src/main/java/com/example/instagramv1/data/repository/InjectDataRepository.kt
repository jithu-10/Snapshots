package com.example.instagramv1.data.repository

import android.graphics.Bitmap
import com.example.instagramv1.data.entities.Post
import java.util.*

interface InjectDataRepository {

    suspend fun addAccount(email : String,
                   phone : String,
                   password : String,
                   createdDate : Date,
                   userName : String,
                   fullName: String,
                   profilePicture: Bitmap?,
                   privateAccount: Boolean,
                   userId : Int,
                   accountId : Int)

    suspend fun addUser(userName : String,
                fullName : String,
                profilePicture : Bitmap?,
                privateAccount : Boolean,userId: Int) : Long


    suspend fun addPost(userId : Int,
                createdDate: Date,
                image : Bitmap,
                description : String?,
                location : String?,postId : Int)

    suspend fun addConnection(following_user_id : Int, followed_user_id : Int)

    suspend fun addComment(userId: Int, postId : Int, commentedTime : Date , description: String)



}