package com.example.instagramv1.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "searchHistory")
data class SearchHistory(
    @ColumnInfo(name = "history_text") val historyText : String,
    @ColumnInfo(name = "user_id") val userId : Int
){
    @PrimaryKey(autoGenerate = true)
    var id : Int = 0
}
