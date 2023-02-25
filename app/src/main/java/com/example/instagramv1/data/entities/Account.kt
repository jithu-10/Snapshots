package com.example.instagramv1.data.entities


import androidx.room.*
import java.util.Date


@Entity(tableName = "account")
data class Account(
    @ColumnInfo(name = "email") val email : String,
    @ColumnInfo(name = "phone") val phone : String,
    @ColumnInfo(name = "password") val password : String,
    @ColumnInfo(name = "created_date") val createdDate : Date,
    @ColumnInfo(name = "user_name") val userName : String,
    @ColumnInfo(name = "user_id") val userId : Int,
    @PrimaryKey(autoGenerate = true)
    var accountId : Int = 0
){

}

