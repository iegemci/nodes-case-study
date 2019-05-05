package com.enesgemci.mymovies.view.like

import android.animation.ArgbEvaluator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorInt

private const val DOTS_COUNT = 7
private const val OUTER_DOTS_POSITION_ANGLE = 51

/**
 * cloned and converted to kotlin
 * source: https://github.com/jd-alexander/LikeButton
 */
class DotsView : View {

    private var COLOR_1 = -0x3ef9
    private var COLOR_2 = -0x6800
    private var COLOR_3 = -0xa8de
    private var COLOR_4 = -0xbbcca

    private var innerWidth = 0
    private var innerHeight = 0

    private val circlePaints = arrayOfNulls<Paint>(4)

    private var centerX: Int = 0
    private var centerY: Int = 0

    private var maxOuterDotsRadius: Float = 0.toFloat()
    private var maxInnerDotsRadius: Float = 0.toFloat()
    private var maxDotSize: Float = 0.toFloat()

    var currentProgress = 0f
        set(currentProgress) {
            field = currentProgress

            updateInnerDotsPosition()
            updateOuterDotsPosition()
            updateDotsPaints()
            updateDotsAlpha()

            postInvalidate()
        }

    private var currentRadius1 = 0f
    private var currentDotSize1 = 0f

    private var currentDotSize2 = 0f
    private var currentRadius2 = 0f

    private val argbEvaluator = ArgbEvaluator()

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
        for (i in circlePaints.indices) {
            circlePaints[i] = Paint()
            circlePaints[i]?.style = Paint.Style.FILL
            circlePaints[i]?.isAntiAlias = true
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        centerX = w / 2
        centerY = h / 2
        maxDotSize = 5f
        maxOuterDotsRadius = w / 2 - maxDotSize * 2
        maxInnerDotsRadius = 0.8f * maxOuterDotsRadius
    }

    override fun onDraw(canvas: Canvas) {
        drawOuterDotsFrame(canvas)
        drawInnerDotsFrame(canvas)
    }

    private fun drawOuterDotsFrame(canvas: Canvas) {
        for (i in 0 until DOTS_COUNT) {
            val cX =
                (centerX + currentRadius1 * Math.cos(i.toDouble() * OUTER_DOTS_POSITION_ANGLE.toDouble() * Math.PI / 180)).toInt()
            val cY =
                (centerY + currentRadius1 * Math.sin(i.toDouble() * OUTER_DOTS_POSITION_ANGLE.toDouble() * Math.PI / 180)).toInt()
            canvas.drawCircle(cX.toFloat(), cY.toFloat(), currentDotSize1, circlePaints[i % circlePaints.size])
        }
    }

    private fun drawInnerDotsFrame(canvas: Canvas) {
        for (i in 0 until DOTS_COUNT) {
            val cX = (centerX + currentRadius2 * Math.cos((i * OUTER_DOTS_POSITION_ANGLE - 10) * Math.PI / 180)).toInt()
            val cY = (centerY + currentRadius2 * Math.sin((i * OUTER_DOTS_POSITION_ANGLE - 10) * Math.PI / 180)).toInt()
            canvas.drawCircle(cX.toFloat(), cY.toFloat(), currentDotSize2, circlePaints[(i + 1) % circlePaints.size])
        }
    }

    private fun updateInnerDotsPosition() {
        currentRadius2 =
            if (currentProgress < 0.3f) {
                Utils.mapValueFromRangeToRange(
                    currentProgress.toDouble(),
                    0.0,
                    0.3,
                    0.0,
                    maxInnerDotsRadius.toDouble()
                ).toFloat()
            } else {
                maxInnerDotsRadius
            }
        currentDotSize2 = when {
            currentProgress == 0f -> 0f
            currentProgress < 0.2 -> maxDotSize
            currentProgress < 0.5 -> Utils.mapValueFromRangeToRange(
                currentProgress.toDouble(),
                0.2,
                0.5,
                maxDotSize.toDouble(),
                0.3 * maxDotSize
            ).toFloat()
            else -> Utils.mapValueFromRangeToRange(
                currentProgress.toDouble(),
                0.5,
                1.0,
                (maxDotSize * 0.3f).toDouble(), 0.0
            ).toFloat()
        }

    }

    private fun updateOuterDotsPosition() {
        currentRadius1 =
            if (currentProgress < 0.3f) {
                Utils.mapValueFromRangeToRange(
                    currentProgress.toDouble(),
                    0.0,
                    0.3,
                    0.0,
                    (maxOuterDotsRadius * 0.8f).toDouble()
                ).toFloat()
            } else {
                Utils.mapValueFromRangeToRange(
                    currentProgress.toDouble(),
                    0.3,
                    1.0,
                    (0.8f * maxOuterDotsRadius).toDouble(),
                    maxOuterDotsRadius.toDouble()
                ).toFloat()
            }

        currentDotSize1 = when {
            currentProgress == 0f -> 0f
            currentProgress < 0.7 -> maxDotSize
            else -> Utils.mapValueFromRangeToRange(
                currentProgress.toDouble(),
                0.7,
                1.0,
                maxDotSize.toDouble(),
                0.0
            ).toFloat()
        }
    }

    private fun updateDotsPaints() {
        if (currentProgress < 0.5f) {
            val progress = Utils.mapValueFromRangeToRange(
                currentProgress.toDouble(),
                0.0,
                0.5,
                0.0,
                1.0
            ).toFloat()

            circlePaints[0]?.color = argbEvaluator.evaluate(progress, COLOR_1, COLOR_2) as Int
            circlePaints[1]?.color = argbEvaluator.evaluate(progress, COLOR_2, COLOR_3) as Int
            circlePaints[2]?.color = argbEvaluator.evaluate(progress, COLOR_3, COLOR_4) as Int
            circlePaints[3]?.color = argbEvaluator.evaluate(progress, COLOR_4, COLOR_1) as Int
        } else {
            val progress = Utils.mapValueFromRangeToRange(
                currentProgress.toDouble(),
                0.5,
                1.0,
                0.0,
                1.0
            ).toFloat()

            circlePaints[0]?.color = argbEvaluator.evaluate(progress, COLOR_2, COLOR_3) as Int
            circlePaints[1]?.color = argbEvaluator.evaluate(progress, COLOR_3, COLOR_4) as Int
            circlePaints[2]?.color = argbEvaluator.evaluate(progress, COLOR_4, COLOR_1) as Int
            circlePaints[3]?.color = argbEvaluator.evaluate(progress, COLOR_1, COLOR_2) as Int
        }
    }

    fun setColors(@ColorInt primaryColor: Int, @ColorInt secondaryColor: Int) {
        COLOR_1 = primaryColor
        COLOR_2 = secondaryColor
        COLOR_3 = primaryColor
        COLOR_4 = secondaryColor

        invalidate()
    }

    private fun updateDotsAlpha() {
        val progress = Utils.clamp(currentProgress.toDouble(), 0.6, 1.0).toFloat()
        val alpha = Utils.mapValueFromRangeToRange(
            progress.toDouble(),
            0.6,
            1.0,
            255.0,
            0.0
        ).toInt()

        circlePaints[0]?.alpha = alpha
        circlePaints[1]?.alpha = alpha
        circlePaints[2]?.alpha = alpha
        circlePaints[3]?.alpha = alpha
    }

    fun setSize(width: Int, height: Int) {
        innerWidth = width
        innerHeight = height
        invalidate()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        if (innerWidth != 0 && innerHeight != 0)
            setMeasuredDimension(innerWidth, innerHeight)
    }
}