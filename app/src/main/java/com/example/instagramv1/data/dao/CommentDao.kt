package com.example.instagramv1.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.instagramv1.data.entities.Comment
import com.example.instagramv1.model.CommentViewData
import com.example.instagramv1.ui.authscreen.commentscreen.CommentViewModel

@Dao
interface CommentDao {

    @Query("SELECT comment.comment_id, comment.post_id,user.user_id AS comment_owner_user_id, user.user_name AS comment_owner_user_name, user.profile_picture AS comment_owner_profile_picture, comment.description as comment_description, COUNT(commentReaction.user_id) AS comment_reaction_count, CASE WHEN EXISTS (SELECT 1 FROM commentReaction WHERE comment_id = comment.comment_id AND user_id = :userId) THEN 1 ELSE 0 END AS user_reacted\n" +
            "FROM comment\n" +
            "INNER JOIN user ON comment.user_id = user.user_id\n" +
            "LEFT JOIN commentReaction ON comment.comment_id = commentReaction.comment_id\n" +
            "WHERE Comment.post_id = :postId\n" +
            "GROUP BY comment.comment_id ORDER BY comment.comment_id DESC")
    fun getComments(postId : Int,userId : Int) : LiveData<List<CommentViewData>>

    @Query("SELECT COUNT(*) FROM comment WHERE post_id = :postId")
    fun getNumberOfComments(postId: Int) : LiveData<Int>

    @Insert
    suspend fun insertComment(comment : Comment) : Long

    @Query("DELETE FROM comment WHERE comment_id = :commentId")
    suspend fun deleteComment(commentId : Int)

    @Query("SELECT comment_id FROM comment WHERE post_id = :postId")
    suspend fun getCommentsOfPost(postId: Int) : List<Int>

    @Query("DELETE FROM comment WHERE post_id = :postId")
    suspend fun deleteCommentsOfPost(postId: Int)

    @Query("SELECT user_id FROM comment WHERE comment_id = :commentId")
    suspend fun getCommentOwnerId(commentId: Int) : Int

    @Query("SELECT post_id FROM comment WHERE comment_id = :commentId")
    suspend fun getPostFromComment(commentId: Int) : Int

}