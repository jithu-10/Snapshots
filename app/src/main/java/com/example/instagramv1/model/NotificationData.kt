package com.example.instagramv1.model

import android.graphics.Bitmap

data class NotificationData(
    val broadcaster_user_name : String,
    val broadcaster_profile_picture : Bitmap?,
    val broadcaster_user_id : Int,
    val message : String
)
