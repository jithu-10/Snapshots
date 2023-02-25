package com.example.instagramv1.model

import android.content.ClipDescription
import android.graphics.Bitmap

data class PostData(
    val userId : Int,
    val image : Bitmap,
    val location : String?,
    val description: String?
)
