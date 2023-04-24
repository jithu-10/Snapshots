package com.example.instagramv1.data.dataconvertors

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.util.LruCache
import androidx.room.TypeConverter
import java.io.ByteArrayOutputStream


object BitmapConvertor {


    private val bitmapCache = LruCache<String, Bitmap>(100)

    @TypeConverter
    fun toBitmap(byteArray: ByteArray?): Bitmap? {
        Log.d("Image Check",byteArray.toString())
        return byteArray?.let {
            //BitmapFactory.decodeByteArray(byteArray,0,byteArray.size)
            if(byteArray.isEmpty()){
                null
            }
            else{
                val key = byteArray.contentHashCode().toString()
                val bitmap = bitmapCache.get(key) ?: BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
                if (bitmap !in bitmapCache.snapshot().values) {
                    bitmapCache.put(key, bitmap)
                }

                bitmap
            }

        }
    }

    @TypeConverter
    fun fromBitmap(bitmap: Bitmap?): ByteArray?{
        val outputStream = ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.PNG,100,outputStream)
        return outputStream.toByteArray()
    }

}
