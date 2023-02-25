package com.example.instagramv1.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.instagramv1.data.entities.Reaction
import com.example.instagramv1.data.entities.SavedPost

@Dao
interface SavedPostDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSavedPost(savedPost: SavedPost)

    @Query("SELECT user_id FROM savedPost WHERE post_id = :postId")
    suspend fun getSavedPost(postId : Int) : List<Int>

    @Query("SELECT EXISTS(SELECT * FROM savedPost WHERE post_id = :postId AND user_id = :userId)")
    fun getSavedStatus(postId: Int, userId: Int) : LiveData<Boolean>

    @Query("Delete from savedPost WHERE user_id = :userId AND post_id = :postId")
    suspend fun deleteSavedPost(userId : Int, postId: Int)

}