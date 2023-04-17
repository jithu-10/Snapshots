package com.example.instagramv1.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notificationCount")
data class NotificationCount(
    @ColumnInfo(name = "user_id") @PrimaryKey val user_id : Int ,
    @ColumnInfo(name="count") val count : Int
)
