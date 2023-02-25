package com.example.instagramv1.utils

import android.graphics.Bitmap
import android.media.Image
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat

import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.instagramv1.R





@BindingAdapter("app:imageBitmap")
fun loadImage(iv: ImageView,bitmap: Bitmap?){
    if(bitmap == null){
        iv.setImageDrawable(ResourcesCompat.getDrawable(iv.resources,R.drawable.default_profile_pic,null))
    }
    else{
        Glide.with(iv)
            .load(bitmap)
            .into(iv)

    }
}

