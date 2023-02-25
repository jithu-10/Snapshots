package com.example.instagramv1.model

import android.graphics.Bitmap

data class UserMiniProfileData(
    val user_id : Int,
    val user_name : String,
    val full_name : String,
    val profile_picture : Bitmap?,
    val private_account : Boolean
)
