package com.example.instagramv1.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "commentReaction")
data class CommentReaction(
    @ColumnInfo(name = "comment_id") val commentId : Int,
    @ColumnInfo(name = "user_id") val userId : Int
){
    @PrimaryKey(autoGenerate = true)
    var id : Int = 0
}