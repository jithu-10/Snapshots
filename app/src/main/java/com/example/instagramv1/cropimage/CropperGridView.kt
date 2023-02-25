package com.example.instagramv1.cropimage


import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.os.Handler
import android.util.AttributeSet
import android.view.View
import com.example.instagramv1.R


class CropperGridView : View {
    private val HIDE_INTERVAL: Long = 1500
    private var mPaint: Paint? = null
    private var mColor = 0xfffffff
    private var mAlpha = 200
    private var mStrokeWidth = 3
    private var showGrid = false
    private var mHandler: Handler? = null
    private var mPath: Path? = null

    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context) {
        init(context, attrs)
    }



    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY)
        setMeasuredDimension(width, height)

    }

    private fun init(context: Context, attrs: AttributeSet?) {
        if (attrs != null) {
            val mTypedArray =
                context.obtainStyledAttributes(attrs, R.styleable.cropper__CropperView)
            mColor = mTypedArray.getColor(
                R.styleable.cropper__CropperView_cropper__grid_color,
                mColor
            )
            var alpha = 255 * mTypedArray.getFloat(
                R.styleable.cropper__CropperView_cropper__grid_opacity,
                1f
            )
            if (alpha < 0) {
                alpha = 0f
            } else if (alpha > 255) {
                alpha = 255f
            }
            mAlpha = alpha.toInt()
            mStrokeWidth = mTypedArray.getDimensionPixelOffset(
                R.styleable.cropper__CropperView_cropper__grid_thickness,
                mStrokeWidth
            )
            mTypedArray.recycle()
        }
        mPaint = Paint()
        mPaint!!.color = mColor
        mPaint!!.isAntiAlias = true
        mPaint!!.style = Paint.Style.STROKE
        mPaint!!.strokeCap = Paint.Cap.ROUND
        mPaint!!.strokeWidth = mStrokeWidth.toFloat()
        mPaint!!.alpha = mAlpha
        mPath = Path()
        mHandler = Handler()
        if (isInEditMode) {
            showGrid = true
        }
    }

    public override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (!showGrid) {
            return
        }
        val width = canvas.width
        val height = canvas.height
        mPath!!.reset()
        mPath!!.moveTo((width / 3).toFloat(), 0f)
        mPath!!.lineTo((width / 3).toFloat(), height.toFloat())
        mPath!!.moveTo((2 * width / 3).toFloat(), 0f)
        mPath!!.lineTo((2 * width / 3).toFloat(), height.toFloat())
        mPath!!.moveTo(0f, (height / 3).toFloat())
        mPath!!.lineTo(width.toFloat(), (height / 3).toFloat())
        mPath!!.moveTo(0f, (2 * height / 3).toFloat())
        mPath!!.lineTo(width.toFloat(), (2 * height / 3).toFloat())
        canvas.drawPath(mPath!!, mPaint!!)
    }



    fun setShowGrid(showGrid: Boolean) {
        if (this.showGrid != showGrid) {
            this.showGrid = showGrid
            if (this.showGrid) {
                mHandler!!.removeCallbacks(mHideRunnable)
                invalidate()
                return
            }
            mHandler!!.postDelayed(mHideRunnable, HIDE_INTERVAL)
        }
    }

    private val mHideRunnable = Runnable {
        showGrid = false
        invalidate()
    }


}