package com.example.instagramv1.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.instagramv1.data.entities.Reaction

@Dao
interface ReactionDao{

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReaction(reaction: Reaction)

    @Query("SELECT user_id FROM reaction WHERE post_id = :postId")
    suspend fun getReactions(postId : Int) : List<Int>

    @Query("Delete from reaction WHERE user_id = :userId AND post_id = :postId")
    suspend fun deleteReaction(userId : Int, postId: Int)

    @Query("SELECT EXISTS(SELECT * FROM reaction where user_id  = :userId AND post_id = :postId)")
    fun isUserReacted(userId: Int,postId: Int) : LiveData<Boolean>

    @Query("SELECT COUNT(*) FROM reaction WHERE post_id = :postId")
    fun getNumberOfReaction(postId: Int) : LiveData<Int>





}