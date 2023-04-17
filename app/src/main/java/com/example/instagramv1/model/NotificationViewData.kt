package com.example.instagramv1.model

import android.graphics.Bitmap

data class NotificationViewData(
    val notification_id : Int,
    val broadcaster_user_name : String,
    val broadcaster_profile_picture : Bitmap?,
    val broadcaster_user_id : Int,
    val notification_type : NotificationType,
    val content_id : Int
)
