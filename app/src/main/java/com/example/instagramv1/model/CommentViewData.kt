package com.example.instagramv1.model

import android.graphics.Bitmap
import java.util.*

data class CommentViewData(
    val comment_id : Int,
    val post_id : Int,
    val comment_owner_user_id : Int,
    val comment_owner_user_name : String,
    val comment_owner_profile_picture : Bitmap?,
    val comment_description : String,
    val comment_created_time : Date,
    var comment_reaction_count : Int,
    var user_reacted : Boolean

)
