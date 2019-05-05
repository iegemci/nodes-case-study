package com.enesgemci.mymovies.view.like

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.VectorDrawable
import android.util.TypedValue
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat

/**
 * cloned and converted to kotlin
 * source: https://github.com/jd-alexander/LikeButton
 */
object Utils {

    fun mapValueFromRangeToRange(
        value: Double,
        fromLow: Double,
        fromHigh: Double,
        toLow: Double,
        toHigh: Double
    ): Double {
        return toLow + (value - fromLow) / (fromHigh - fromLow) * (toHigh - toLow)
    }

    fun clamp(value: Double, low: Double, high: Double): Double {
        return Math.min(Math.max(value, low), high)
    }

    fun resizeDrawable(context: Context, drawable: Drawable, width: Int, height: Int): Drawable {
        val bitmap = getBitmap(drawable, width, height)
        return BitmapDrawable(
            context.resources,
            Bitmap.createScaledBitmap(bitmap, width, height, true)
        )
    }

    fun getBitmap(drawable: Drawable, width: Int, height: Int): Bitmap {
        return when (drawable) {
            is BitmapDrawable -> drawable.bitmap
            is VectorDrawableCompat -> getBitmap(drawable, width, height)
            is VectorDrawable -> getBitmap(drawable, width, height)
            else -> throw IllegalArgumentException("Unsupported drawable type")
        }
    }

    private fun getBitmap(vectorDrawable: VectorDrawable, width: Int, height: Int): Bitmap {
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        vectorDrawable.run {
            setBounds(0, 0, canvas.width, canvas.height)
            draw(canvas)
        }
        return bitmap
    }

    private fun getBitmap(vectorDrawable: VectorDrawableCompat, width: Int, height: Int): Bitmap {
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        vectorDrawable.setBounds(0, 0, canvas.width, canvas.height)
        vectorDrawable.draw(canvas)
        return bitmap
    }

    fun dipToPixels(context: Context, dipValue: Float): Float {
        val metrics = context.resources.displayMetrics
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, metrics)
    }
}

