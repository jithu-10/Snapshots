package com.example.instagramv1.data.dao

import androidx.room.*
import com.example.instagramv1.data.entities.CommentReaction

@Dao
interface CommentReactionDao{

    @Insert
    suspend fun insertCommentReaction(commentReaction: CommentReaction)

    @Query("SELECT user_id FROM commentReaction WHERE comment_id = :commentId")
    suspend fun getReactions(commentId : Int) : List<Int>

    @Query("Delete from commentReaction WHERE user_id = :userId AND comment_id = :commentId")
    suspend fun deleteReaction(userId : Int, commentId : Int)

}