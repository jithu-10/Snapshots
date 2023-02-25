package com.example.instagramv1.cropimage

import android.graphics.Bitmap
import android.graphics.Rect


data class CropInfo @JvmOverloads constructor(
    val x: Int,
    val y: Int,
    val width: Int,
    val height: Int,
    val addPadding: Boolean = false,
    val horizontalPadding: Int = 0,
    val verticalPadding: Int = 0,
    val paddingColor: Int = -1
) {




    override fun toString(): String {
        return "CropInfo{" +
                "x=" + x +
                ", y=" + y +
                ", width=" + width +
                ", height=" + height +
                ", addPadding=" + addPadding +
                ", verticalPadding=" + verticalPadding +
                ", horizontalPadding=" + horizontalPadding +
                '}'
    }

    companion object {
        fun cropCompleteBitmap(
            bitmap: Bitmap,
            paddingToMakeSquare: Boolean,
            horizontalPadding: Int,
            verticalPadding: Int,
            paddingColor: Int
        ): CropInfo {
            return CropInfo(
                0,
                0,
                bitmap.width,
                bitmap.height,
                paddingToMakeSquare,
                horizontalPadding,
                verticalPadding,
                paddingColor
            )
        }

        fun cropFromRect(
            rect: Rect,
            paddingToMakeSquare: Boolean,
            horizontalPadding: Int,
            verticalPadding: Int,
            paddingColor: Int
        ): CropInfo {
            return CropInfo(
                rect.left,
                rect.top,
                rect.width(),
                rect.height(),
                paddingToMakeSquare,
                horizontalPadding,
                verticalPadding,
                paddingColor
            )
        }
    }
}