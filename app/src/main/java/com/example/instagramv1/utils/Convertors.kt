package com.example.instagramv1.utils

import android.widget.EditText
import androidx.databinding.InverseMethod

object Convertors {

    @InverseMethod("stringToDate")
    @JvmStatic fun dateToString(
        value: Long
    ): String {
        if(value.toInt()==0){
            return ""
        }
        return value.toString()
    }

    @JvmStatic fun stringToDate(
        value: String
    ): Long {
        return value.toLong()
    }
}