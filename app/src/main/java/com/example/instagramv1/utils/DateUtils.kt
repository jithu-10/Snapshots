package com.example.instagramv1.utils

import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

object DateUtils {


    fun setCreatedTime(createdTime : Date) : String{
        val currentTime = Date()
        val durationInMillis = currentTime.time -  createdTime.time
        val days = TimeUnit.MILLISECONDS.toDays(durationInMillis).toInt()
        val hours = TimeUnit.MILLISECONDS.toHours(durationInMillis).toInt()
        val minutes = TimeUnit.MILLISECONDS.toMinutes(durationInMillis).toInt()
        val seconds = TimeUnit.MILLISECONDS.toSeconds(durationInMillis).toInt()
        if(days==0){
            return if(hours==0){
                if(minutes == 0){
                    if(seconds == 0){
                        "Just now"
                    } else{
                        "$seconds ${if(seconds==1) "second" else "seconds"} ago"
                    }
                } else{
                    "$minutes ${if(minutes==1) "minute" else "minutes"} ago"
                }
            } else{
                "$hours ${if(hours==1) "hour" else "hours"} ago"
            }
        }
        else if(days<7){
            return  "$days ${if(days==1) "day" else "days"} ago"
        }
        else if(days in 7..14){
            return  "1 week ago"
        }
        else{
            val month = getMonthStringFromDate(createdTime)
            val year = getYearFromDate(createdTime)
            val day = getDayFromDate(createdTime)

            val currentYear = getYearFromDate(currentTime)


            return if(currentYear == year){
                "$day $month"
            } else{
                "$day $month $year"
            }
        }
    }

    private fun getYearFromDate(date: Date): Int {
        val calendar = Calendar.getInstance()
        calendar.time = date
        return calendar.get(Calendar.YEAR)
    }

    private fun getMonthStringFromDate(date: Date): String {
        val calendar = Calendar.getInstance()
        calendar.time = date
        return SimpleDateFormat("MMMM", Locale.getDefault()).format(calendar.time)
    }

    private fun getDayFromDate(date: Date): Int {
        val calendar = Calendar.getInstance()
        calendar.time = date
        return calendar.get(Calendar.DAY_OF_MONTH)
    }
}