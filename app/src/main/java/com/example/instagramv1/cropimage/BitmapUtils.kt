package com.example.instagramv1.cropimage

import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Rect
import android.net.Uri

object BitmapUtils {

    fun getCroppedBitmap(bitmap: Bitmap?, cropInfo: CropInfo): Bitmap? {
        return if (!cropInfo.addPadding) {
            Bitmap.createBitmap(bitmap!!, cropInfo.x, cropInfo.y, cropInfo.width, cropInfo.height)
        } else addPadding(bitmap, cropInfo, cropInfo.paddingColor)
    }


    private fun addPadding(bmp: Bitmap?, info: CropInfo, color: Int): Bitmap? {
        if (bmp == null) {
            return null
        }
        var bitmap: Bitmap? = null
        return try {
            val biggerParam = Math.max(
                info.width + 2 * info.horizontalPadding,
                info.height + 2 * info.verticalPadding
            )
            bitmap = Bitmap.createBitmap(biggerParam, biggerParam, bmp.config)
            val canvas = Canvas(bitmap)
            canvas.drawColor(color)
            val dest = Rect(
                info.horizontalPadding,
                info.verticalPadding,
                info.horizontalPadding + info.width,
                info.verticalPadding + info.height
            )
            val src = Rect(info.x, info.y, info.x + info.width, info.y + info.height)
            canvas.drawBitmap(bmp, src, dest, null)
            bitmap
        } catch (e: OutOfMemoryError) {
            if (bitmap != null && !bitmap.isRecycled) {
                bitmap.recycle()
                bitmap = null
            }
            throw e
        }
    }

    fun getFilePathFromUri(context: Context, uri: Uri): String? {

        if ("content".equals(uri.scheme, ignoreCase = true)) {
            return getDataColumn(context, uri, null, null)
        } else if ("file".equals(uri.scheme, ignoreCase = true)) {
            return uri.path
        }
        return null
    }


    private fun getDataColumn(
        context: Context, uri: Uri?, selection: String?,
        selectionArgs: Array<String>?
    ): String? {
        var cursor: Cursor? = null
        val column = "_data"
        val projection = arrayOf(column)
        try {
            cursor = context.contentResolver
                .query(uri!!, projection, selection, selectionArgs, null)
            if (cursor != null && cursor.moveToFirst()) {
                val column_index = cursor.getColumnIndexOrThrow(column)
                return cursor.getString(column_index)
            }
        } finally {
            cursor?.close()
        }
        return null
    }



    fun rotateBitmap(source: Bitmap, angle: Float): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(angle)
        return Bitmap.createBitmap(source, 0, 0, source.width, source.height, matrix, true)
    }
}
