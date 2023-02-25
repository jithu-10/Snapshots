package com.example.instagramv1.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import java.util.*


@Entity(tableName = "comment")
data class Comment(
    @ColumnInfo(name = "user_id")val userId : Int,
    @ColumnInfo(name = "post_id")val postId : Int,
    @ColumnInfo(name = "commented_time")val createdTime : Date,
    @ColumnInfo(name = "description")val description : String,
){
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "comment_id")
    var commentId : Int = 0
}

