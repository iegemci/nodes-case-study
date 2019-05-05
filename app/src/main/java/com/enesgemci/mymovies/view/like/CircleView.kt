package com.enesgemci.mymovies.view.like

import android.animation.ArgbEvaluator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorInt

/**
 * cloned and converted to kotlin
 * source: https://github.com/jd-alexander/LikeButton
 */
class CircleView : View {

    @ColorInt
    var startColor = -0xa8de
        set(@ColorInt value) {
            field = value
            invalidate()
        }

    @ColorInt
    var endColor = -0x3ef9
        set(@ColorInt value) {
            field = value
            invalidate()
        }

    private val argbEvaluator = ArgbEvaluator()

    private val circlePaint = Paint()
    private val maskPaint = Paint()

    private var tempBitmap: Bitmap? = null
    private var tempCanvas: Canvas? = null

    var outerCircleRadiusProgress = 0f
        set(outerCircleRadiusProgress) {
            field = outerCircleRadiusProgress
            updateCircleColor()
            postInvalidate()
        }

    var innerCircleRadiusProgress = 0f
        set(innerCircleRadiusProgress) {
            field = innerCircleRadiusProgress
            postInvalidate()
        }

    private var innerWidth = 0
    private var innerHeight = 0

    private var maxCircleSize: Int = 0

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int)
            : super(context, attrs, defStyleAttr) {
        init()
    }


    private fun init() {
        circlePaint.style = Paint.Style.FILL
        circlePaint.isAntiAlias = true
        maskPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        maskPaint.isAntiAlias = true
    }

    fun setSize(width: Int, height: Int) {
        this.innerWidth = width
        this.innerHeight = height
        invalidate()
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        if (innerWidth != 0 && innerHeight != 0)
            setMeasuredDimension(innerWidth, innerHeight)
    }


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        maxCircleSize = w / 2
        tempBitmap = Bitmap.createBitmap(width, width, Bitmap.Config.ARGB_8888)
        tempCanvas = Canvas(tempBitmap)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        tempCanvas?.let {
            it.drawColor(0xffffff, PorterDuff.Mode.CLEAR)
            it.drawCircle(
                (width / 2).toFloat(),
                (height / 2).toFloat(),
                outerCircleRadiusProgress * maxCircleSize,
                circlePaint
            )
            it.drawCircle(
                (width / 2).toFloat(),
                (height / 2).toFloat(),
                innerCircleRadiusProgress * maxCircleSize + 1,
                maskPaint
            )

            tempBitmap?.let {
                canvas.drawBitmap(it, 0f, 0f, null)
            }
        }
    }

    private fun updateCircleColor() {
        var colorProgress = Utils.clamp(
            outerCircleRadiusProgress.toDouble(),
            0.5,
            1.0
        ).toFloat()
        colorProgress = Utils.mapValueFromRangeToRange(
            colorProgress.toDouble(),
            0.5,
            1.0,
            0.0,
            1.0
        ).toFloat()
        circlePaint.color = argbEvaluator.evaluate(colorProgress, startColor, endColor) as Int
    }
}