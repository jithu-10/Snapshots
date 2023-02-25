package com.example.instagramv1.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Date


@Entity(tableName = "connection")
data class Connection(
    @ColumnInfo(name = "following_user_id") val followingUserId : Int,
    @ColumnInfo(name = "followed_user_id") val followedUserId : Int
){
    @PrimaryKey(autoGenerate = true)
    var id : Int = 0
}
// following_user_id  who follows another one
// followed_user_id  who get followed by another


//select * from user left join connection on user_id = followed_user_id where following_user_id is null or following_user_id <> 2 ;
//insert into connection values(1,2);

//select * from user left join connection on user_id = followed_user_id WHERE (following_user_id IS NULL OR following_user_id <> 1) AND user_id <> 1;