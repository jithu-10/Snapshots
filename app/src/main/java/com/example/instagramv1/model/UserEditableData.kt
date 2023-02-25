package com.example.instagramv1.model

import android.graphics.Bitmap

data class UserEditableData(
    val email : String ,
    val phone : String ,
    val user_name : String ,
    val full_name : String ,
    val profile_picture : Bitmap?
)
