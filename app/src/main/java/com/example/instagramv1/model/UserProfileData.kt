package com.example.instagramv1.model


import android.graphics.Bitmap

data class  UserProfileData(
    val user_id : Int,
    val user_name : String,
    val full_name : String,
    val profile_picture : Bitmap?,
    val private_account : Boolean,
    val num_posts : Int,
    val num_following : Int,
    val num_followers : Int
)