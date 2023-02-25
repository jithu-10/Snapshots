package com.example.instagramv1.data.entities

import android.graphics.Bitmap
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class User(
    @ColumnInfo(name = "user_name")var userName : String,
    @ColumnInfo(name = "full_name")var fullName : String,
    @ColumnInfo(name = "profile_picture")var profilePicture : Bitmap?,
    @ColumnInfo(name = "private_account")var privateAccount : Boolean = false,
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "user_id")
    var userId : Int = 0
){

}
