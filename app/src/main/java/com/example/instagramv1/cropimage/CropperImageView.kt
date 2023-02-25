package com.example.instagramv1.cropimage


import android.content.Context
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.Log
import android.view.GestureDetector
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.ScaleGestureDetector.SimpleOnScaleGestureListener
import androidx.appcompat.widget.AppCompatImageView
import com.example.instagramv1.R


class CropperImageView : AppCompatImageView {
    private val mMatrixValues = FloatArray(9)
    protected var mGestureDetector: GestureDetector? = null
    protected var mScaleDetector: ScaleGestureDetector? = null
    private var mMinZoom = 0f
    private var mMaxZoom = 0f
    private var mBaseZoom = 0f
    private var mBaseZoomBigger = 0f
    private var mFocusX = 0f
    private var mFocusY = 0f
    private var isMaxZoomSet = false
    private var isMinZoomSetByUser = false
    private var mFirstRender = true
    var loadedBitmap: Bitmap? = null
        private set
    var isMakeSquare = true
    private var mGestureCallback: GestureCallback? = null
    private var showAnimation = false
    private var isAdjusting = false
    private var isCropping = false
    var paddingColor = Color.WHITE
    var isGestureEnabled = true
    var DEBUG1 = false
    var isInitWithFitToCenter = false

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



    fun setDEBUG(DEBUG: Boolean) {
        this.DEBUG1 = DEBUG
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        if (attrs != null) {
            val mTypedArray =
                context.obtainStyledAttributes(attrs, R.styleable.cropper__CropperView)
            if (mTypedArray != null) {
                paddingColor = mTypedArray.getColor(
                    R.styleable.cropper__CropperView_cropper__padding_color,
                    paddingColor
                )
                isMakeSquare = mTypedArray.getBoolean(
                    R.styleable.cropper__CropperView_cropper__add_padding_to_make_square,
                    true
                )
                isInitWithFitToCenter = mTypedArray.getBoolean(
                    R.styleable.cropper__CropperView_cropper__fit_to_center,
                    false
                )
                mTypedArray.recycle()
            }
        }
        val mGestureListener: GestureListener = GestureListener()
        mGestureDetector = GestureDetector(context, mGestureListener, null, true)
        val mScaleListener: ScaleListener = ScaleListener()
        mScaleDetector = ScaleGestureDetector(context, mScaleListener)
        scaleType = ScaleType.MATRIX
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY)
        setMeasuredDimension(width, height)

    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        if (changed || mFirstRender) {
            if (mFirstRender) {
                val drawable = drawable ?: return
                val orientation = resources.configuration.orientation
                if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    mBaseZoom = (right - left).toFloat() / Math.max(
                        drawable.intrinsicHeight,
                        drawable.intrinsicWidth
                    )
                    mBaseZoomBigger = (right - left).toFloat() / Math.min(
                        drawable.intrinsicHeight,
                        drawable.intrinsicWidth
                    )
                } else {
                    mBaseZoom = (bottom - top).toFloat() / Math.max(
                        drawable.intrinsicHeight,
                        drawable.intrinsicWidth
                    )
                    Log.d(
                        "Special",
                        drawable.intrinsicWidth.toString() + " " + drawable.intrinsicHeight
                    )
                    mBaseZoomBigger = (bottom - top).toFloat() / Math.min(
                        drawable.intrinsicHeight,
                        drawable.intrinsicWidth
                    )
                }
                if ( isMaxZoomSet && mBaseZoom > mMaxZoom) {
                    // base zoom should not be greater than max zoom.
                    mBaseZoom = mMaxZoom
                    mBaseZoomBigger = mMaxZoom
                    if (mMinZoom > mMaxZoom) {
                        Log.e(
                            TAG,
                            "min zoom is greater than max zoom. Changing min zoom = max zoom"
                        )
                        _setMinZoom(mMaxZoom)
                    }
                }
                if (mMinZoom <= 0 || !isMinZoomSetByUser) {
                    _setMinZoom(mBaseZoom)
                }
                if (isInitWithFitToCenter) {

                } else {
                    cropToCenter(drawable, bottom - top)
                }
                mFirstRender = false
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (isAdjusting) {
            return true
        }
        if (isCropping) {
            Log.e(TAG, "Cropping current bitmap. Can't perform this action right now.")
            return true
        }
        val action = event.actionMasked
        if (action == MotionEvent.ACTION_DOWN) {
            if (mGestureCallback != null) {
                mGestureCallback?.onGestureStarted()
            }
        }
        mScaleDetector!!.onTouchEvent(event)
        if (!mScaleDetector!!.isInProgress) {
            mGestureDetector!!.onTouchEvent(event)
        }
        when (event.action) {
            MotionEvent.ACTION_UP -> {
                if (mGestureCallback != null) {
                    mGestureCallback?.onGestureCompleted()
                }
                return onUp()
            }
        }
        return true
    }

    override fun setImageBitmap(bm: Bitmap?) {
        if (isCropping) {
            Log.e(TAG, "Cropping current bitmap. Can't set bitmap now")
            return
        }
        mFirstRender = true
        if (bm == null) {
            loadedBitmap = null
            super.setImageBitmap(null)
            return
        }
        if (bm.height > 1280 || bm.width > 1280 && DEBUG1) {
            Log.w(TAG, "Bitmap size greater than 1280. This might cause memory issues")
        }
        loadedBitmap = bm
        super.setImageBitmap(bm)
        requestLayout()
    }


    private fun cropToCenter(drawable: Drawable?, frameDimen: Int) {
        if (drawable == null) {
            if (DEBUG1) {
                Log.e(TAG, "Drawable is null. I can't fit anything")
            }
            return
        }
        if (frameDimen == 0) {
            if (DEBUG1) {
                Log.e(TAG, "Frame Dimension is 0. I'm quite boggled by it.")
            }
            return
        }
        val width = drawable.intrinsicWidth
        val height = drawable.intrinsicHeight
        if (DEBUG1) {
            Log.i(TAG, "drawable size: ($width ,$height)")
        }
        val min_dimen = Math.min(width, height)
        val scaleFactor = min_dimen.toFloat() / frameDimen.toFloat()
        //Scale Factor = Dimension of new shape / Dimension of old shape
        val matrix = Matrix()
        matrix.setScale(1f / scaleFactor, 1f / scaleFactor)
        matrix.postTranslate(
            (frameDimen - width / scaleFactor) / 2,
            (frameDimen - height / scaleFactor) / 2
        )
        imageMatrix = matrix
    }



    fun onScroll(distanceX: Float, distanceY: Float): Boolean {
        val matrix = imageMatrix
        matrix.postTranslate(-distanceX, -distanceY)
        imageMatrix = matrix
        invalidate()
        return true
    }

    private fun onUp(): Boolean {
        val drawable = drawable ?: return false

        // If over scrolled, return back to the place.
        val matrix = imageMatrix
        val tx = getMatrixValue(matrix, Matrix.MTRANS_X)
        val ty = getMatrixValue(matrix, Matrix.MTRANS_Y)
        val scaleX = getMatrixValue(matrix, Matrix.MSCALE_X)
        val scaleY = getMatrixValue(matrix, Matrix.MSCALE_Y)
        if (DEBUG1) {
            Log.i(TAG, "onUp: $tx $ty")
            Log.i(TAG, "scale x: $scaleX")
            Log.i(TAG, "scale y: $scaleY")
            Log.i(
                TAG,
                "min, max, base zoom: $mMinZoom, $mMaxZoom, $mBaseZoom"
            )
            Log.i(TAG, "imageview size: $width $height")
            Log.i(TAG, "drawable size: " + drawable.intrinsicWidth + " " + drawable.intrinsicHeight)
            Log.i(
                TAG,
                "scaled drawable size: " + scaleX * drawable.intrinsicWidth + " " + scaleY * drawable.intrinsicHeight
            )
            Log.i(TAG, "h diff: " + (scaleY * drawable.intrinsicHeight + ty - height))
        }
        if (scaleX < mMinZoom && mMinZoom >= mBaseZoom) {
            if (DEBUG1) {
                Log.i(
                    TAG,
                    "set scale to min zoom: $mMinZoom"
                )
            }
            var xx = width / 2 - mMinZoom * drawable.intrinsicWidth / 2
            var yy = height / 2 - mMinZoom * drawable.intrinsicHeight / 2
            if (drawable.intrinsicHeight > drawable.intrinsicWidth) {
                yy = if (ty >= 0) {
                    0f
                } else if (ty + scaleY * drawable.intrinsicHeight <= height) {
                    height - mMinZoom * drawable.intrinsicHeight
                } else {
                    // We want to change scale based on P1. P1 is (x, mFocusY). x does not matter here. Consider 1D.
                    // We translate P1 to origin (x, mFocusY) -> (x, 0)
                    // Apply zoom on origin -> (minzoom/scale)
                    // Translate origin to P1
                    (ty - mFocusY) * (mMinZoom / scaleY) + mFocusY
                }
            } else {
                xx = if (tx >= 0) {
                    0f
                } else if (tx + scaleX * drawable.intrinsicWidth <= width) {
                    width - mMinZoom * drawable.intrinsicWidth
                } else {
                    // We want to change scale based on P1. P1 is (mFocusX, y). y does not matter here. Consider 1D.
                    // We translate P1 to origin (mFocusX, y) -> (0, y)
                    // Apply zoom on origin -> (minzoom/scale)
                    // Translate origin to P1
                    (tx - mFocusX) * (mMinZoom / scaleX) + mFocusX
                }
            }
            matrix.reset()
            matrix.setScale(mMinZoom, mMinZoom)
            matrix.postTranslate(xx, yy)
            imageMatrix = matrix
            invalidate()
            if (DEBUG1) {
                Log.i(TAG, "scale after invalidate: " + getScale(matrix))
            }

            return true
        } else if (scaleX <= mBaseZoom || scaleX <= mBaseZoomBigger) {

            // align to center for the smaller dimension
            val h = drawable.intrinsicHeight
            val w = drawable.intrinsicWidth
            val xTranslate: Float
            val yTranslate: Float
            if (h <= w) {
                yTranslate = height / 2 - scaleX * h / 2
                xTranslate = if (tx >= 0) {
                    0f
                } else {
                    val xDiff = width - scaleX * drawable.intrinsicWidth
                    if (tx < xDiff) {
                        xDiff
                    } else {
                        tx
                    }
                }
            } else {
                xTranslate = width / 2 - scaleX * w / 2
                yTranslate = if (ty >= 0) {
                    0f
                } else {
                    val yDiff = height - scaleY * drawable.intrinsicHeight
                    if (ty < yDiff) {
                        yDiff
                    } else {
                        ty
                    }
                }
            }
            matrix.reset()
            matrix.postScale(scaleX, scaleX)
            matrix.postTranslate(xTranslate, yTranslate)
            imageMatrix = matrix
            invalidate()

            return true
        } else if (isMaxZoomSet && scaleX > mMaxZoom) {
            if (DEBUG1) {
                Log.i(TAG, "set to max zoom")
                Log.i(TAG, "isMaxZoomSet: $isMaxZoomSet")
            }
            matrix.postScale(mMaxZoom / scaleX, mMaxZoom / scaleX, mFocusX, mFocusY)
            imageMatrix = matrix
            invalidate()

            return true
        }
        adjustToSides()
        return true
    }

    private fun adjustToSides(): Boolean {
        var changeRequired = false
        val drawable = drawable ?: return false
        val matrix = imageMatrix
        var tx = getMatrixValue(matrix, Matrix.MTRANS_X)
        var ty = getMatrixValue(matrix, Matrix.MTRANS_Y)
        val scaleX = getMatrixValue(matrix, Matrix.MSCALE_X)
        val scaleY = getMatrixValue(matrix, Matrix.MSCALE_Y)
        if (tx > 0) {
            tx = -tx
            changeRequired = true
        } else {

            // check if scrolled to left
            val xDiff = width - scaleX * drawable.intrinsicWidth
            if (tx < xDiff) {
                tx = xDiff - tx
                changeRequired = true
            } else {
                tx = 0f
            }
        }
        if (ty > 0) {
            ty = -ty
            changeRequired = true
        } else {

            // check if scrolled to top
            val yDiff = height - scaleY * drawable.intrinsicHeight
            if (ty < yDiff) {
                ty = yDiff - ty
                changeRequired = true
            } else {
                ty = 0f
            }
        }
        if (changeRequired) {
            matrix.postTranslate(tx, ty)
            imageMatrix = matrix
            invalidate()
        }
        return changeRequired
    }



    private fun getMatrixValue(matrix: Matrix, whichValue: Int): Float {
        matrix.getValues(mMatrixValues)
        return mMatrixValues[whichValue]
    }

    private fun getScale(matrix: Matrix): Float {
        return getMatrixValue(matrix, Matrix.MSCALE_X)
    }

    var maxZoom: Float
        get() = mMaxZoom
        set(zoom) {
            if (zoom <= 0) {
                Log.e(TAG, "Max zoom must be greater than 0")
                return
            }
            if (mMinZoom > 0 && zoom < mMinZoom) {
                Log.e(TAG, "Max zoom must be greater than min zoom")
                return
            }
            mMaxZoom = zoom
            isMaxZoomSet = true

        }
    var minZoom: Float
        get() = mMinZoom
        set(zoom) {
            if (_setMinZoom(zoom)) {
                isMinZoomSetByUser = true
            }
        }

    private fun _setMinZoom(zoom: Float): Boolean {
        if (zoom <= 0) {
            Log.e(TAG, "Min zoom must be greater than 0")
            return false
        }
        if (isMaxZoomSet && zoom > mMaxZoom) {
            Log.e(TAG, "Min zoom must not be greater than max zoom")
            return false
        }
        isMinZoomSetByUser = false
        mMinZoom = zoom
        return true
    }

    fun cropToCenter() {
        if (isCropping) {
            Log.e(TAG, "Cropping current bitmap. Can't perform this action right now.")
            return
        }
        val drawable = drawable
        drawable?.let { cropToCenter(it, width) }
    }




    fun cropBitmap(): Bitmap? {
        if (isCropping) {
            Log.e(TAG, "Cropping current bitmap. Can't perform this action right now.")
            return null
        }
        isCropping = true
        return try {
            val bitmap = croppedBitmap
            isCropping = false
            bitmap
        } catch (e: OutOfMemoryError) {
            isCropping = false
            throw e
        }
    }


    val cropInfo: CropInfo?
        get() {
            if (loadedBitmap == null) {
                Log.e(TAG, "original image is not available")
                return null
            }
            val matrix = imageMatrix
            val xTrans = getMatrixValue(matrix, Matrix.MTRANS_X)
            val yTrans = getMatrixValue(matrix, Matrix.MTRANS_Y)
            val scale = getMatrixValue(matrix, Matrix.MSCALE_X)
            if (DEBUG1) {
                Log.i(
                    TAG,
                    "xTrans: $xTrans, yTrans: $yTrans , scale: $scale"
                )
                Log.i(TAG, "old bitmap: " + loadedBitmap!!.width + " " + loadedBitmap!!.height)
            }

            // No scale/crop required.
            if (xTrans > 0 && yTrans > 0 && scale <= mMinZoom) {
                // Add padding if not square
                val verticalPadding =
                    if (loadedBitmap!!.height > loadedBitmap!!.width) 0 else (loadedBitmap!!.width - loadedBitmap!!.height) / 2
                val horizontalPadding =
                    if (loadedBitmap!!.width > loadedBitmap!!.height) 0 else (loadedBitmap!!.height - loadedBitmap!!.width) / 2
                return CropInfo.cropCompleteBitmap(
                    loadedBitmap!!,
                    isMakeSquare, horizontalPadding, verticalPadding,
                    paddingColor
                )
            }
            var cropY = -yTrans / scale
            val Y = height / scale
            var cropX = -xTrans / scale
            val X = width / scale
            if (DEBUG1) {
                Log.i(TAG, "cropY: $cropY")
                Log.i(TAG, "Y: $Y")
                Log.i(TAG, "cropX: $cropX")
                Log.i(TAG, "X: $X")
            }
            if (cropY + Y > loadedBitmap!!.height) {
                cropY = loadedBitmap!!.height - Y
                if (DEBUG1) {
                    Log.i(TAG, "readjust cropY to: $cropY")
                }
            } else if (cropY < 0) {
                cropY = 0f
                if (DEBUG1) {
                    Log.i(TAG, "readjust cropY to: $cropY")
                }
            }
            if (cropX + X > loadedBitmap!!.width) {
                cropX = loadedBitmap!!.width - X
                if (DEBUG1) {
                    Log.i(TAG, "readjust cropX to: $cropX")
                }
            } else if (cropX < 0) {
                cropX = 0f
                if (DEBUG1) {
                    Log.i(TAG, "readjust cropX to: $cropX")
                }
            }
            val rect: Rect
            var horizontalPadding = 0
            var verticalPadding = 0
            var isPaddingRequired = true
            if (loadedBitmap!!.height > loadedBitmap!!.width) {
                // Height is greater than width.
                if (xTrans >= 0) {
                    // Image is zoomed. Crop from height
                    rect = Rect(0, cropY.toInt(), loadedBitmap!!.width, (Y + cropY).toInt())
                    horizontalPadding = ((Y - loadedBitmap!!.width) / 2).toInt()
                } else {
                    // Crop from width and height both
                    rect =
                        Rect(cropX.toInt(), cropY.toInt(), (cropX + X).toInt(), (cropY + Y).toInt())
                    isPaddingRequired = false
                }
            } else {
                if (yTrans >= 0) {
                    // Image is zoomed. Crop from width and add padding to make square
                    rect = Rect(cropX.toInt(), 0, (cropX + X).toInt(), loadedBitmap!!.height)
                    verticalPadding = ((X - loadedBitmap!!.height) / 2).toInt()
                } else {
                    // Crop from width and height both.
                    rect =
                        Rect(cropX.toInt(), cropY.toInt(), (cropX + X).toInt(), (cropY + Y).toInt())
                    isPaddingRequired = false
                }
            }
            isPaddingRequired =
                isPaddingRequired && (horizontalPadding != 0 || verticalPadding != 0)
            return CropInfo.cropFromRect(
                rect, isMakeSquare && isPaddingRequired, horizontalPadding, verticalPadding,
                paddingColor
            )
        }


    fun getCroppedBitmap(cropInfo: CropInfo?): Bitmap? {
        if (loadedBitmap == null) {
            Log.e(TAG, "original image is not available")
            return null
        }
        return BitmapUtils.getCroppedBitmap(loadedBitmap, cropInfo!!)
    }


    private val croppedBitmap: Bitmap?
        private get() {
            val cropInfo: CropInfo? = cropInfo
            return if (cropInfo != null) {
                getCroppedBitmap(cropInfo)
            } else null
        }





    private inner class GestureListener : SimpleOnGestureListener() {
        override fun onScroll(
            e1: MotionEvent,
            e2: MotionEvent,
            distanceX: Float,
            distanceY: Float
        ): Boolean {
            if (!isGestureEnabled) {
                return false
            }
            if (isCropping) {
                Log.e(TAG, "Cropping current bitmap. Can't perform this action right now.")
                return false
            }
            if (e1 == null || e2 == null) {
                return false
            }
            if (e1.pointerCount > 1 || e2.pointerCount > 1) {
                return false
            }
            this@CropperImageView.onScroll(distanceX, distanceY)
            return false
        }
    }

    private inner class ScaleListener : SimpleOnScaleGestureListener() {
        override fun onScale(detector: ScaleGestureDetector): Boolean {
            if (!isGestureEnabled) {
                return false
            }
            if (isCropping) {
                Log.e(TAG, "Cropping current bitmap. Can't perform this action right now.")
                return false
            }
            val matrix = imageMatrix
            // This does the trick!
            mFocusX = detector.focusX
            mFocusY = detector.focusY
            matrix.postScale(
                detector.scaleFactor, detector.scaleFactor,
                detector.focusX, detector.focusY
            )
            imageMatrix = matrix
            invalidate()
            return true
        }
    }

    fun setGestureCallback(mGestureCallback: GestureCallback?) {
        this.mGestureCallback = mGestureCallback
    }

    interface GestureCallback {
        fun onGestureStarted()
        fun onGestureCompleted()
    }






    companion object {
        private const val TAG = "CropperImageView"
    }
}