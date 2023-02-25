package com.example.instagramv1.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "notification")
data class Notification(
    @ColumnInfo(name = "broadcaster_user_id") val broadcaster_user_id : Int,
    @ColumnInfo(name = "receiver_user_id") val receiver_user_id : Int,
    @ColumnInfo(name = "message") val message : String,
    @ColumnInfo(name = "created_time")val createdTime : Date,
    @ColumnInfo(name = "notification_id")
    @PrimaryKey(autoGenerate = true)
    var id : Int = 0
)
