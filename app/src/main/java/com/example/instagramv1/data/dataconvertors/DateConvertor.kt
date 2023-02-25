package com.example.instagramv1.data.dataconvertors

import androidx.room.TypeConverter
import java.util.*

object DateConvertor {

    @TypeConverter
    fun toDate(dateLong: Long?): Date? {
        return dateLong?.let { Date(it) }
    }

    @TypeConverter
    fun fromDate(date: Date?): Long? {
        return date?.time
    }
}