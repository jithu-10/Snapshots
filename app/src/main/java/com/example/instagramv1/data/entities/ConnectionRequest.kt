package com.example.instagramv1.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "connectionRequest")
data class ConnectionRequest(
    @ColumnInfo(name = "following_user_id") val followingUserId : Int,
    @ColumnInfo(name = "followed_user_id") val followedUserId : Int
){
    @PrimaryKey(autoGenerate = true)
    var id : Int = 0
}