package com.example.instagramv1.utils

import android.os.Handler
import android.os.Looper
import android.view.View

abstract class DelayClickListener : View.OnClickListener {
    abstract fun onDelayClick()
    override fun onClick(v: View?) {
        val handler = Handler(Looper.getMainLooper())
        v?.isClickable = false
        onDelayClick()
        handler.postDelayed({
            v?.isClickable= true
        },500)
    }
}