package com.example.instagramv1.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.example.instagramv1.data.entities.Account
import com.example.instagramv1.data.entities.Post
import com.example.instagramv1.data.entities.User

@Dao
interface SampleDao {

    @Query("SELECT * FROM user WHERE user_id = :userId")
    fun getUser(userId : Int) : LiveData<User>

    @Query("SELECT * FROM post WHERE post_id = :postId")
    fun getPost(postId : Int) : LiveData<Post>

    @Query("SELECT phone FROM account WHERE user_id = :userId")
    fun getUserPhone(userId : Int) : LiveData<String>

    @Query("SELECT email FROM account WHERE user_id = :userId")
    fun getUserEmail(userId : Int) : LiveData<String>

    @Query("SELECT * FROM account WHERE user_id = :userId")
    fun getAccount(userId: Int) : LiveData<Account>


}