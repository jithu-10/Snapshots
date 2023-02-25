package com.example.instagramv1.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.instagramv1.data.entities.Connection
import com.example.instagramv1.data.entities.ConnectionRequest
import com.example.instagramv1.model.UserConnectionWithOtherUser

@Dao
interface ConnectionDao {

    @Query("SELECT followed_user_id FROM connection WHERE following_user_id = :userId")
    suspend fun getFollowersList(userId : Int) : List<Int>

    @Query("SELECT following_user_id FROM connection WHERE followed_user_id = :userId")
    suspend fun getFollowingList(userId : Int) : List<Int>

    @Insert
    suspend fun addConnection(connection: Connection)

    @Query("SELECT EXISTS(SELECT * FROM connection WHERE following_user_id = :userId AND followed_user_id = :followedUserId)")
    suspend fun isConnected(userId: Int,followedUserId: Int) : Boolean

    @Insert
    suspend fun requestConnection(connectionRequest : ConnectionRequest)

    @Query("DELETE FROM connectionRequest where following_user_id = :followingUserId AND followed_user_id = :followedUserId")
    suspend fun removeConnectionRequest(followingUserId : Int, followedUserId : Int)

    @Query("DELETE FROM connection WHERE following_user_id = :followingUserId AND followed_user_id = :followedUserId")
    suspend fun removeConnection(followingUserId : Int, followedUserId : Int)

    @Query("SELECT \n" +
            "    CASE \n" +
            "        WHEN EXISTS (SELECT 1 FROM connection WHERE following_user_id = :userId AND followed_user_id = :otherUserId) THEN 1 \n" +
            "        WHEN EXISTS (SELECT 1 FROM ConnectionRequest WHERE following_user_id = :userId AND followed_user_id = :otherUserId) THEN 2 \n" +
            "        ELSE 3 END")
    fun getUserConnectionWithOtherUsers(userId: Int,otherUserId : Int) : LiveData<Int>



}