package com.example.instagramv1.utils

import android.content.Context
import android.view.Gravity
import android.view.View
import android.widget.Toast


object HelperUtils {

    fun showToast(message : String,context : Context){
        val toast = Toast.makeText(context, message, Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.show()
    }


}