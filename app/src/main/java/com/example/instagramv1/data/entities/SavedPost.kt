package com.example.instagramv1.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "savedPost")
data class SavedPost(
    @ColumnInfo(name = "post_id") val postId : Int,
    @ColumnInfo(name = "user_id") val userId : Int
){
    @PrimaryKey(autoGenerate = true)
    var id : Int = 0
}