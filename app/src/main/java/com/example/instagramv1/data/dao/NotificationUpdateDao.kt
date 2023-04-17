package com.example.instagramv1.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.instagramv1.data.entities.Comment
import com.example.instagramv1.data.entities.NotificationUpdate

@Dao
interface NotificationUpdateDao {

    @Insert
    suspend fun addNotificationUpdate(notificationUpdate: NotificationUpdate)

    @Query("DELETE FROM notificationUpdate WHERE notification_id IN (\n" +
            "SELECT notification_id FROM notificationUpdate \n"+
            "WHERE broadcaster_user_id = :broadcaster_user_id \n"+
            "AND receiver_user_id = :receiver_user_id \n"+
            "AND content_id = :postId AND notification_type = 3 );")
    suspend fun deletePostLikedNotification(broadcaster_user_id: Int,receiver_user_id: Int,postId : Int)

    @Query("DELETE FROM notificationUpdate WHERE notification_id IN (\n" +
            "SELECT notification_id FROM notificationUpdate \n"+
            "WHERE broadcaster_user_id = :broadcaster_user_id \n"+
            "AND receiver_user_id = :receiver_user_id \n"+
            "AND content_id = :commentId AND notification_type = 4 );")
    suspend fun deletePostCommentedNotification(broadcaster_user_id: Int,receiver_user_id: Int,commentId : Int)

    @Query("DELETE FROM notificationUpdate WHERE notification_id IN (\n" +
            "SELECT notification_id FROM notificationUpdate \n"+
            "WHERE broadcaster_user_id = :broadcaster_user_id \n"+
            "AND receiver_user_id = :receiver_user_id \n"+
            "AND content_id = :postId AND notification_type = 5 );")
    suspend fun deletePostSavedNotification(broadcaster_user_id: Int,receiver_user_id: Int,postId: Int)

    @Query("DELETE FROM notificationUpdate WHERE notification_id IN (\n" +
            "SELECT notification_id FROM notificationUpdate \n"+
            "WHERE broadcaster_user_id = :broadcaster_user_id \n"+
            "AND receiver_user_id = :receiver_user_id \n"+
            "AND content_id = :commentId AND notification_type = 6 );")
    suspend fun deleteCommentLikedNotification(broadcaster_user_id: Int,receiver_user_id: Int,commentId: Int)

    @Query("DELETE FROM notificationUpdate WHERE notification_id IN (\n" +
            "SELECT notification_id FROM notificationUpdate \n"+
            "WHERE content_id = :postId AND (notification_type = 3 OR notification_type = 5 ));")
    suspend fun deleteAllNotificationBasedOnPost(postId: Int)

    @Query("DELETE FROM notificationUpdate WHERE notification_id IN (\n" +
            "SELECT notification_id FROM notificationUpdate \n"+
            "WHERE content_id = :commentId AND ( notification_type = 4 OR notification_type = 6 ));")
    suspend fun deleteAllNotificationBasedOnComment(commentId : Int)
}