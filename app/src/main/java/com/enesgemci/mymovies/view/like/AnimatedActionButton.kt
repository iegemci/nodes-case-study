package com.enesgemci.mymovies.view.like

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.animation.OvershootInterpolator
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import com.enesgemci.mymovies.R

/**
 * cloned and converted to kotlin
 * source: https://github.com/jd-alexander/LikeButton
 */
class AnimatedActionButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr),
    View.OnClickListener {

    lateinit var icon: ImageView
    lateinit var dotsView: DotsView
    lateinit var circleView: CircleView

    var likeListener: OnActionListener? = null
    var animationEndListener: OnAnimationEndListener? = null

    private var circleStartColor: Int = 0
    private var circleEndColor: Int = 0
    private var iconSize: Int = 0

    private var animationScaleFactor: Float = 0.toFloat()

    /**
     * Returns current like state
     *
     * @return current like state
     */
    var isLiked: Boolean = false
        set(value) {
            field = value

            if (value) {
                icon.setImageDrawable(likeDrawable)
            } else {
                icon.setImageDrawable(unLikeDrawable)
            }
        }

    private var animatorSet: AnimatorSet? = null

    private var likeDrawable: Drawable? = null
    private var unLikeDrawable: Drawable? = null

    init {
        if (!isInEditMode) {
            init(context, attrs, defStyleAttr)
        }
    }

    /**
     * Does all the initial setup of the button such as retrieving all the attributes that were
     * set in xml and inflating the like button's view and initial state.
     *
     * @param context
     * @param attrs
     * @param defStyle
     */
    private fun init(context: Context, attrs: AttributeSet?, defStyle: Int) {
        LayoutInflater.from(getContext()).inflate(R.layout.layout_like_view, this, true)
        icon = findViewById(R.id.icon)
        dotsView = findViewById(R.id.dots)
        circleView = findViewById(R.id.circle)

        val array = context.obtainStyledAttributes(attrs, R.styleable.AnimatedActionButton, defStyle, 0)

        val fancyAnimationEnabled = array.getBoolean(R.styleable.AnimatedActionButton_fancy_animation_enabled, false)

        if (fancyAnimationEnabled) {
            dotsView.isVisible = true
        } else {
            dotsView.isInvisible = true
        }

        iconSize = array.getDimensionPixelSize(R.styleable.AnimatedActionButton_icon_size, 40)

        likeDrawable = getDrawableFromResource(array, R.styleable.AnimatedActionButton_like_drawable)

        likeDrawable?.let {
            setLikeDrawable(it)
        }

        unLikeDrawable = getDrawableFromResource(array, R.styleable.AnimatedActionButton_unlike_drawable)

        unLikeDrawable?.let {
            setUnlikeDrawable(it)
        }

        circleStartColor = array.getColor(R.styleable.AnimatedActionButton_circle_start_color, 0)

        if (circleStartColor != 0) {
            circleView.startColor = circleStartColor
        }

        circleEndColor = array.getColor(R.styleable.AnimatedActionButton_circle_end_color, 0)

        if (circleEndColor != 0) {
            circleView.endColor = circleEndColor
        }

        val dotPrimaryColor = array.getColor(R.styleable.AnimatedActionButton_dots_primary_color, 0)
        val dotSecondaryColor = array.getColor(R.styleable.AnimatedActionButton_dots_secondary_color, 0)

        if (dotPrimaryColor != 0 && dotSecondaryColor != 0) {
            dotsView.setColors(dotPrimaryColor, dotSecondaryColor)
        }

        isEnabled = array.getBoolean(R.styleable.AnimatedActionButton_is_enabled, true)

        val status = array.getBoolean(R.styleable.AnimatedActionButton_liked, false)
        setAnimationScaleFactor(array.getFloat(R.styleable.AnimatedActionButton_anim_scale_factor, 3f))
        isLiked = status
        setOnClickListener(this)

        array.recycle()
    }

    private fun getDrawableFromResource(array: TypedArray, styleableIndexId: Int): Drawable? {
        val id = array.getResourceId(styleableIndexId, -1)

        return if (-1 != id) ContextCompat.getDrawable(context, id) else null
    }

    /**
     * This triggers the entire functionality of the button such as icon changes,
     * animations, listeners etc.
     *
     * @param v
     */
    override fun onClick(v: View) {
        if (!isEnabled)
            return

        isLiked = !isLiked

        icon.setImageDrawable(if (isLiked) likeDrawable else unLikeDrawable)

        animatorSet?.cancel()

        if (isLiked) {
            icon.animate().cancel()
            icon.scaleX = 0f
            icon.scaleY = 0f
            circleView.innerCircleRadiusProgress = 0f
            circleView.outerCircleRadiusProgress = 0f
            dotsView.currentProgress = 0f

            animatorSet = AnimatorSet()

            val outerCircleAnimator = ObjectAnimator.ofFloat(
                circleView,
                "outerCircleRadiusProgress",
                0.1f,
                1f
            )

            outerCircleAnimator.duration = 250
            outerCircleAnimator.interpolator = DECCELERATE_INTERPOLATOR

            val innerCircleAnimator = ObjectAnimator.ofFloat(
                circleView,
                "innerCircleRadiusProgress",
                0.1f,
                1f
            )

            innerCircleAnimator.duration = 200
            innerCircleAnimator.startDelay = 200
            innerCircleAnimator.interpolator = DECCELERATE_INTERPOLATOR

            val starScaleYAnimator = ObjectAnimator.ofFloat(icon, ImageView.SCALE_Y, 0.2f, 1f)

            starScaleYAnimator.duration = 350
            starScaleYAnimator.startDelay = 250
            starScaleYAnimator.interpolator = OVERSHOOT_INTERPOLATOR

            val starScaleXAnimator = ObjectAnimator.ofFloat(icon, ImageView.SCALE_X, 0.2f, 1f)
            starScaleXAnimator.duration = 350
            starScaleXAnimator.startDelay = 250
            starScaleXAnimator.interpolator = OVERSHOOT_INTERPOLATOR

            val dotsAnimator = ObjectAnimator.ofFloat(dotsView, "currentProgress", 0f, 1f)
            dotsAnimator.duration = 900
            dotsAnimator.startDelay = 50
            dotsAnimator.interpolator = ACCELERATE_DECELERATE_INTERPOLATOR

            animatorSet?.playTogether(
                outerCircleAnimator,
                innerCircleAnimator,
                starScaleYAnimator,
                starScaleXAnimator,
                dotsAnimator
            )

            animatorSet?.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationCancel(animation: Animator) {
                    circleView.innerCircleRadiusProgress = 0f
                    circleView.outerCircleRadiusProgress = 0f
                    dotsView.currentProgress = 0f
                    icon.scaleX = 1f
                    icon.scaleY = 1f
                }

                override fun onAnimationEnd(animation: Animator) {
                    animationEndListener?.onAnimationEnd(this@AnimatedActionButton)
                    likeListener?.liked(this@AnimatedActionButton)
                }
            })

            animatorSet?.start()
        } else {
            likeListener?.unLiked(this)
        }
    }

    /**
     * Used to trigger the scale animation that takes places on the
     * icon when the button is touched.
     *
     * @param event
     * @return
     */
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (!isEnabled)
            return true

        when (event.action) {
            MotionEvent.ACTION_DOWN ->
                /*
                Commented out this line and moved the animation effect to the action up event due to
                conflicts that were occurring when library is used in sliding type views.

                icon.animate().scaleX(0.7f).scaleY(0.7f).setDuration(150).setInterpolator(DECCELERATE_INTERPOLATOR);
                */
                isPressed = true

            MotionEvent.ACTION_MOVE -> {
                val x = event.x
                val y = event.y
                val isInside = x > 0 && x < width && y > 0 && y < height
                if (isPressed != isInside) {
                    isPressed = isInside
                }
            }

            MotionEvent.ACTION_UP -> {
                icon.animate().scaleX(0.7f).scaleY(0.7f).setDuration(150).interpolator = DECCELERATE_INTERPOLATOR
                icon.animate().scaleX(1f).scaleY(1f).interpolator = DECCELERATE_INTERPOLATOR
                if (isPressed) {
                    performClick()
                    isPressed = false
                }
            }
            MotionEvent.ACTION_CANCEL -> isPressed = false
        }
        return true
    }

    /**
     * This drawable is shown when the button is a liked state.
     *
     * @param resId
     */
    fun setLikeDrawableRes(@DrawableRes resId: Int) {
        likeDrawable = ContextCompat.getDrawable(context, resId)

        likeDrawable?.let {
            if (iconSize != 0) {
                likeDrawable = Utils.resizeDrawable(context, it, iconSize, iconSize)
            }
        }

        if (isLiked) {
            icon.setImageDrawable(likeDrawable)
        }
    }

    /**
     * This drawable is shown when the button is in a liked state.
     *
     * @param likeDrawable
     */
    fun setLikeDrawable(likeDrawable: Drawable) {
        this.likeDrawable = likeDrawable

        if (iconSize != 0) {
            this.likeDrawable = Utils.resizeDrawable(context, likeDrawable, iconSize, iconSize)
        }

        if (isLiked) {
            icon.setImageDrawable(this.likeDrawable)
        }
    }

    /**
     * This drawable will be shown when the button is in on unLiked state.
     *
     * @param resId
     */
    fun setUnlikeDrawableRes(@DrawableRes resId: Int) {
        unLikeDrawable = ContextCompat.getDrawable(context, resId)

        unLikeDrawable?.let {
            if (iconSize != 0) {
                unLikeDrawable = Utils.resizeDrawable(context, it, iconSize, iconSize)
            }
        }

        if (!isLiked) {
            icon.setImageDrawable(unLikeDrawable)
        }
    }

    /**
     * This drawable will be shown when the button is in on unLiked state.
     *
     * @param unLikeDrawable
     */
    fun setUnlikeDrawable(unLikeDrawable: Drawable) {
        this.unLikeDrawable = unLikeDrawable

        if (iconSize != 0) {
            this.unLikeDrawable = Utils.resizeDrawable(context, unLikeDrawable, iconSize, iconSize)
        }

        if (!isLiked) {
            icon.setImageDrawable(this.unLikeDrawable)
        }
    }

    /**
     * Sets the size of the drawable/icon that's being used. The views that generate
     * the like effect are also updated to reflect the size of the icon.
     *
     * @param iconSize
     */

    fun setIconSizeDp(iconSize: Int) {
        setIconSizePx(Utils.dipToPixels(context, iconSize.toFloat()).toInt())
    }

    /**
     * Sets the size of the drawable/icon that's being used. The views that generate
     * the like effect are also updated to reflect the size of the icon.
     *
     * @param iconSize
     */
    fun setIconSizePx(iconSize: Int) {
        this.iconSize = iconSize
        setEffectsViewSize()

        unLikeDrawable?.let {
            this.unLikeDrawable = Utils.resizeDrawable(context, it, iconSize, iconSize)
        }

        likeDrawable?.let {
            this.likeDrawable = Utils.resizeDrawable(context, it, iconSize, iconSize)
        }
    }

    /**
     * This set sets the colours that are used for the little dots
     * that will be exploding once the like button is clicked.
     *
     * @param primaryColor
     * @param secondaryColor
     */
    fun setExplodingDotColorsRes(@ColorRes primaryColor: Int, @ColorRes secondaryColor: Int) {
        dotsView.setColors(
            ContextCompat.getColor(context, primaryColor),
            ContextCompat.getColor(context, secondaryColor)
        )
    }

    fun setExplodingDotColorsInt(@ColorInt primaryColor: Int, @ColorInt secondaryColor: Int) {
        dotsView.setColors(primaryColor, secondaryColor)
    }

    fun setCircleStartColorRes(@ColorRes circleStartColor: Int) {
        this.circleStartColor = ContextCompat.getColor(context, circleStartColor)
        circleView.startColor = this.circleStartColor
    }

    fun setCircleStartColorInt(@ColorInt circleStartColor: Int) {
        this.circleStartColor = circleStartColor
        circleView.startColor = circleStartColor
    }

    fun setCircleEndColorRes(@ColorRes circleEndColor: Int) {
        this.circleEndColor = ContextCompat.getColor(context, circleEndColor)
        circleView.endColor = this.circleEndColor
    }

    /**
     * This function updates the dots view and the circle
     * view with the respective sizes based on the size
     * of the icon being used.
     */
    private fun setEffectsViewSize() {
        if (iconSize != 0) {
            dotsView.setSize((iconSize * animationScaleFactor).toInt(), (iconSize * animationScaleFactor).toInt())
            circleView.setSize(iconSize, iconSize)
        }
    }

    /**
     * Sets the factor by which the dots should be sized.
     */
    fun setAnimationScaleFactor(animationScaleFactor: Float) {
        this.animationScaleFactor = animationScaleFactor

        setEffectsViewSize()
    }

    interface OnAnimationEndListener {

        fun onAnimationEnd(animatedActionButton: AnimatedActionButton)
    }

    interface OnActionListener {

        fun liked(animatedActionButton: AnimatedActionButton)

        fun unLiked(animatedActionButton: AnimatedActionButton)
    }

    companion object {

        private val DECCELERATE_INTERPOLATOR = DecelerateInterpolator()
        private val ACCELERATE_DECELERATE_INTERPOLATOR = AccelerateDecelerateInterpolator()
        private val OVERSHOOT_INTERPOLATOR = OvershootInterpolator(4f)
    }
}
