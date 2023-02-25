package com.example.instagramv1.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.instagramv1.data.entities.Notification

@Dao
interface NotificationDao {

    @Insert
    suspend fun addNotification(notification: Notification)

    @Query("DELETE FROM notification\n" +
            "WHERE notification_id IN (\n" +
            "  SELECT notification_id FROM notification \n" +
            "  WHERE broadcaster_user_id = :broadcaster_user_id \n" +
            "  AND receiver_user_id = :receiver_user_id \n" +
            "  AND message = :message \n" +
            "  LIMIT 1\n" +
            ");")
    suspend fun deleteNotification(broadcaster_user_id : Int, receiver_user_id : Int,message : String)
}