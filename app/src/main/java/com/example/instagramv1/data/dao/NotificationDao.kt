package com.example.instagramv1.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.instagramv1.data.entities.Notification
import com.example.instagramv1.data.entities.NotificationCount

@Dao
interface NotificationDao {

    @Insert
    suspend fun addNotification(notification: Notification)

    @Insert
    suspend fun insertNotificationCount(notificationCount: NotificationCount)

    @Query("UPDATE notificationCount SET count = count + 1 WHERE user_id = :userId")
    suspend fun addNotificationCount(userId : Int)

    @Query("UPDATE notificationCount SET count = 0 WHERE user_id = :userId")
    suspend fun clearNotificationCount(userId: Int)

    @Query("DELETE FROM notification\n" +
            "WHERE notification_id IN (\n" +
            "  SELECT notification_id FROM notification \n" +
            "  WHERE broadcaster_user_id = :broadcaster_user_id \n" +
            "  AND receiver_user_id = :receiver_user_id \n" +
            "  AND message = :message \n" +
            "  LIMIT 1\n" +
            ");")
    suspend fun deleteNotification(broadcaster_user_id : Int, receiver_user_id : Int,message : String)


    @Query("UPDATE notificationCount SET count = count - 1 WHERE user_id = :userId")
    suspend fun decreaseNotificationCount(userId: Int)

    @Query("SELECT count FROM notificationCount WHERE user_id = :userId")
    fun getNotificationCount(userId: Int) : LiveData<Int>
}