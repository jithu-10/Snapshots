package com.example.instagramv1.data.dataconvertors

import androidx.room.TypeConverter
import com.example.instagramv1.model.NotificationType

object NotificationTypeConvertor {

    @TypeConverter
    fun toNotificationType(value : Int?) : NotificationType?{
        return value?.let {
            when(it){
                1 -> NotificationType.FOLLOWS_YOU
                2 -> NotificationType.UNFOLLOWS_YOU
                3 -> NotificationType.LIKED_YOUR_POST
                4 -> NotificationType.COMMENTED_ON_YOUR_POST
                5 -> NotificationType.SAVED_YOUR_POST
                6 -> NotificationType.LIKED_YOUR_COMMENT
                else -> null
            }
        }
    }

    @TypeConverter
    fun fromNotificationType(notificationType: NotificationType?) : Int?{
        return notificationType?.value
    }
}