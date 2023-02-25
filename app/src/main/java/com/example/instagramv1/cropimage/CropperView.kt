package com.example.instagramv1.cropimage


import android.content.Context
import android.content.res.Configuration
import android.graphics.Bitmap
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ViewGroup
import android.widget.FrameLayout


class CropperView : FrameLayout {
    private var mImageView: CropperImageView? = null
    private var mGridView: CropperGridView? = null

    private var isGestureInProgress = false
    private var gridCallback: GridCallback? = null
    private var gestureEnabled = true

    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context, attrs)
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY)
        setMeasuredDimension(width,height)
    }



    private fun init(context: Context, attrs: AttributeSet?) {
        mImageView = CropperImageView(context, attrs)
        mGridView = CropperGridView(context, attrs)
        val params = LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            0
        )
        if (context.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            params.width = 0
            params.height = ViewGroup.LayoutParams.MATCH_PARENT
        }
        addView(mImageView, 0, params)
        addView(mGridView, 1, params)
        mImageView?.setGestureCallback(TouchGestureCallback())
    }



    fun setImageBitmap(bm: Bitmap?) {
        mImageView?.setImageBitmap(bm)
    }



    val croppedBitmap: BitmapResult
        get() = if (isGestureInProgress) {
            BitmapResult.gestureFailure()
        } else try {
            BitmapResult.success(mImageView?.cropBitmap())
        } catch (e: OutOfMemoryError) {
            throw e
        }




    var maxZoom: Float
        get() = mImageView?.maxZoom!!
        set(zoom) {
            mImageView?.maxZoom = zoom
        }


    fun cropToCenter() {
        mImageView?.cropToCenter()
    }



    fun setDebug(status: Boolean) {
        mImageView?.setDEBUG(status)
    }





    private inner class TouchGestureCallback : CropperImageView.GestureCallback {
        override fun onGestureStarted() {
            isGestureInProgress = false
            mGridView?.setShowGrid(gridCallback == null || gridCallback?.onGestureStarted()!!)
        }

        override fun onGestureCompleted() {
            isGestureInProgress = false
            mGridView?.setShowGrid(gridCallback != null && gridCallback?.onGestureCompleted()!!)
        }
    }

    fun setGridCallback(gridCallback: GridCallback?) {
        this.gridCallback = gridCallback
    }

    fun isGestureEnabled(): Boolean {
        return gestureEnabled
    }

    fun setGestureEnabled(enabled: Boolean) {
        gestureEnabled = enabled
        mImageView?.isGestureEnabled = enabled
    }

    interface GridCallback {

        fun onGestureStarted(): Boolean


        fun onGestureCompleted(): Boolean
    }

    companion object {
        private const val TAG = "CropperView"
    }
}