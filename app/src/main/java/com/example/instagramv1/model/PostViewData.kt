package com.example.instagramv1.model

import android.graphics.Bitmap

data class PostViewData(
    val post_owner_id : Int,
    val post_owner_user_name : String,
    val post_owner_profile_picture : Bitmap?,
    val post_id : Int,
    val post_image : Bitmap,
    val post_description : String?,
    val post_location : String?,
    var post_reaction_count : Int,
    val post_comments_count : Int,
    var user_reacted : Boolean

)
