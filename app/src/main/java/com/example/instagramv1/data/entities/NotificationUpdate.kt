package com.example.instagramv1.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.instagramv1.model.NotificationType
import java.util.*

@Entity(tableName = "notificationUpdate")
data class NotificationUpdate(
    @ColumnInfo(name = "broadcaster_user_id") val broadcaster_user_id : Int,
    @ColumnInfo(name = "receiver_user_id") val receiver_user_id : Int,
    @ColumnInfo(name = "created_time")val createdTime : Date,
    @ColumnInfo(name = "notification_type")val notificationType : NotificationType,
    @ColumnInfo(name = "content_id")val contentId : Int? = null,
    @ColumnInfo(name = "notification_id")
    @PrimaryKey(autoGenerate = true)
    var id : Int = 0
)




