package com.example.instagramv1.data.entities


import android.graphics.Bitmap
import androidx.room.*
import java.util.*


@Entity(tableName = "post")
data class Post(
    @ColumnInfo(name = "user_id")val userId : Int,
    @ColumnInfo(name = "created_time")val createdTime : Date,
    @ColumnInfo(name = "image") val image : Bitmap,
    @ColumnInfo(name = "description")var description : String?,
    @ColumnInfo(name = "location")var location : String?,
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "post_id")
    var postId : Int = 0
){

}

