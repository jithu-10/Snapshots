package com.example.instagramv1.cropimage

import android.graphics.Bitmap


class BitmapResult private constructor(val bitmap: Bitmap?, val state: CropState) {

    companion object {
        fun gestureFailure(): BitmapResult {
            return BitmapResult(null, CropState.FAILURE_GESTURE_IN_PROCESS)
        }

        fun success(bitmap: Bitmap?): BitmapResult {
            return BitmapResult(bitmap, CropState.SUCCESS)
        }

        fun error(): BitmapResult {
            return BitmapResult(null, CropState.ERROR)
        }
    }
}
