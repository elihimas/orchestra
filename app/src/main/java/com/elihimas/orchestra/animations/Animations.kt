package com.elihimas.orchestra.animations

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.graphics.Rect
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewPropertyAnimator
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.core.animation.doOnEnd
import com.elihimas.orchestra.OrchestraConfiguration
import kotlin.math.hypot

enum class Direction {
    Up, Down, Left, Right;

    fun reverse(): Direction =
            when (this) {
                Up -> Down
                Down -> Up
                Left -> Right
                Right -> Left
            }
}

abstract class Animation(var duration: Long = OrchestraConfiguration.General.duration,
        //TODO make spacing work (for instance for Form Example)
                         var spacing: Long = OrchestraConfiguration.General.spacing) : Cloneable {
    internal var start = 0F
        set(value) {
            deltaTime = end - value
            field = value
        }
    internal var end = 0F
        set(value) {
            deltaTime = value - start
            field = value
        }
    var delay = 0L

    var deltaTime = 0F

    //TODO: verify if this is being used
    open fun beforeAnimation(view: View) {}

    open fun runAnimation(view: View, endAction: Runnable?) {
        beforeAnimation(view)

        addAnimation(view, view.animate().also {
            it.withEndAction(endAction)
                    .setDuration(duration)
                    .setListener(null)
        })
    }

    open fun addAnimation(view: View, animation: ViewPropertyAnimator) {}

    public abstract override fun clone(): Any

    internal fun cloneFromTo(from: Animation, to: Animation) {
        to.duration = from.duration
        to.spacing = from.spacing
    }

    open fun updateAnimationByTime(view: View, time: Float) {
        val proportion = 1 - (end - time) / deltaTime

        if (proportion in 0.0..1.0) {
            updateAnimationByProportion(view, proportion)
        }
    }

    open fun updateAnimationByProportion(view: View, proportion: Float) {
        TODO("Not yet implemented")
    }

    open fun init(vararg views: View) {}

    open fun calculateDuration(): Long = duration + delay

    open fun updateAnimationTimeBounds(baseTime: Float) {
        start = baseTime + delay
        end = start + duration
    }
}

open class FadeInAnimation(var initialAlpha: Float = 0f, var finalAlpha: Float = 1f) : Animation(600) {

    private var valueDelta = 0f

    override fun init(vararg views: View) {
        valueDelta = finalAlpha - initialAlpha
    }

    override fun updateAnimationByProportion(view: View, proportion: Float) {
        view.alpha = initialAlpha + proportion * valueDelta
    }

    override fun clone(): Any {
        return FadeInAnimation(initialAlpha, finalAlpha).also {
            cloneFromTo(it, this)
        }
    }

    override fun beforeAnimation(view: View) {
        view.alpha = initialAlpha
    }

    override fun addAnimation(view: View, animation: ViewPropertyAnimator) {
        animation.alpha(finalAlpha)
    }
}

class FadeOutAnimation(initialAlpha: Float = 1f, finalAlpha: Float = 0f) : FadeInAnimation(initialAlpha, finalAlpha) {

    override fun clone(): Any {
        return FadeOutAnimation(initialAlpha, finalAlpha).also {
            cloneFromTo(it, this)
        }
    }

}

//TODO for now this class creates it's own ticker
class CircularRevealAnimation : Animation() {

    var animationStarted = false
    override fun updateAnimationByProportion(view: View, proportion: Float) {
        //TODO not working for multiple views
        if (!animationStarted) {
            view.post {
                animationStarted = true
                runAnimation(view, null)
            }
        }
    }

    override fun clone(): Any {
        return CircularRevealAnimation().also {
            cloneFromTo(it, this)
        }
    }

    override fun runAnimation(view: View, endAction: Runnable?) {
        val cx = view.width / 2
        val cy = view.height / 2

        // get the final radius for the clipping circle
        val finalRadius = hypot(cx.toDouble(), cy.toDouble()).toFloat()

        // create the animator for this view (the start radius is zero)
        val anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, 0f, finalRadius)
        // make the view visible and start the animation
        view.visibility = View.VISIBLE
        anim.duration = duration

        anim.doOnEnd { endAction?.run() }

        anim.start()
    }

    override fun addAnimation(view: View, animation: ViewPropertyAnimator) {

    }
}

/**
 * Slide in and out animation implementation
 *
 * @param direction the direction towards to the view should be animated
 * @param reverseAnimation if the animation is a slide out animation
 */
open class SlideAnimation(var direction: Direction, private val reverseAnimation: Boolean = false) : Animation() {

    val update: (view: View, proportion: Float) -> Unit
    var initialTranslationX = 0f
    var initialTranslationY = 0f

    init {
        val actualDirection = if (reverseAnimation) {
            direction.reverse()
        } else {
            direction
        }

        if (reverseAnimation) {
            update = when (actualDirection) {
                Direction.Up -> { view, proportion ->
                    val down = view.height * proportion

                    view.clipBounds = Rect(0, 0, view.width, (view.height - down).toInt())
                    view.translationY = down + initialTranslationY
                }
                Direction.Down -> { view, proportion ->
                    val top = view.height * proportion

                    view.clipBounds = Rect(0, top.toInt(), view.width, view.height)
                    view.translationY = -top + initialTranslationY
                }
                Direction.Left -> { view, proportion ->
                    val right = view.width * proportion

                    view.clipBounds = Rect(0, 0, (view.width - right).toInt(), view.height)
                    view.translationX = right + initialTranslationX
                }
                Direction.Right -> { view, proportion ->
                    val left = view.width * proportion

                    view.clipBounds = Rect(left.toInt(), 0, view.width, view.height)
                    view.translationX = -left + initialTranslationX
                }
            }
        } else {
            update = when (actualDirection) {
                Direction.Up -> { view, proportion ->
                    val down = view.height * proportion

                    view.clipBounds = Rect(0, 0, view.width, down.toInt())
                    view.translationY = view.height - down + initialTranslationY
                }
                Direction.Down -> { view, proportion ->
                    val top = view.height * proportion

                    view.clipBounds = Rect(0, view.height - top.toInt(), view.width, view.height)
                    view.translationY = -view.height + top + initialTranslationY
                }
                Direction.Left -> { view, proportion ->
                    val right = view.width * proportion

                    view.clipBounds = Rect(0, 0, right.toInt(), view.height)
                    view.translationX = view.width - right + initialTranslationX
                }
                Direction.Right -> { view, proportion ->
                    val left = view.width * proportion

                    view.clipBounds = Rect(view.width - left.toInt(), 0, view.width, view.height)
                    view.translationX = -view.width + left + initialTranslationX
                }
            }
        }
    }

    override fun clone(): Any {
        return SlideAnimation(direction, reverseAnimation).also {
            cloneFromTo(it, this)
        }
    }

    override fun init(vararg views: View) {
        views.forEach { view -> view.visibility = View.VISIBLE }

        initialTranslationX = views[0].translationX
        initialTranslationY = views[0].translationY
    }

    override fun updateAnimationByProportion(view: View, proportion: Float) = update(view, proportion)

    override fun runAnimation(view: View, endAction: Runnable?) {
        view.visibility = View.VISIBLE

        val animator = if (reverseAnimation) {
            ValueAnimator.ofFloat(0f, 1f)
        } else {
            ValueAnimator.ofFloat(1f, 0f)
        }

        val actualDirection = if (reverseAnimation) {
            direction.reverse()
        } else {
            direction
        }

        animator.apply {
            when (actualDirection) {
                Direction.Up -> addUpdateListener {
                    val value = this.animatedValue as Float
                    val down = view.height * value

                    view.clipBounds = Rect(0, 0, view.width, (view.height - down).toInt())
                    view.translationY = down
                }
                Direction.Down -> addUpdateListener {
                    val value = this.animatedValue as Float
                    val top = view.height * value

                    view.clipBounds = Rect(0, top.toInt(), view.width, view.height)
                    view.translationY = -top
                }
                Direction.Left -> addUpdateListener {
                    val value = this.animatedValue as Float
                    val right = view.width * value

                    view.clipBounds = Rect(0, 0, (view.width - right).toInt(), view.height)
                    view.translationX = right
                }
                Direction.Right -> addUpdateListener {
                    val value = this.animatedValue as Float
                    val left = view.width * value

                    view.clipBounds = Rect(left.toInt(), 0, view.width, view.height)
                    view.translationX = -left
                }
            }

            doOnEnd {
                if (reverseAnimation) {
                    view.visibility = View.INVISIBLE
                    view.clipBounds = null
                    view.translationX = 0.0f
                    view.translationY = 0.0f
                }
                endAction?.run()
            }

            this.duration = this@SlideAnimation.duration
        }
        animator.start()
    }
}

class SlideOutAnimation(direction: Direction) : SlideAnimation(direction, true)

class TranslateAnimation(internal val x: Float, internal val y: Float) : Animation() {

    private var initialX = 0f
    private var initialY = 0f

    override fun init(vararg views: View) {
        initialX = views[0].translationX
        initialY = views[0].translationY
    }

    override fun updateAnimationByProportion(view: View, proportion: Float) {
        val newX = initialX + proportion * x
        val newY = initialY + proportion * y

        view.translationX = newX
        view.translationY = newY
    }

    override fun clone(): Any {
        return TranslateAnimation(x, y).also {
            cloneFromTo(it, this)
        }
    }

    override fun addAnimation(view: View, animation: ViewPropertyAnimator) {
        animation
                .translationX(x)
                .translationY(y)
    }
}

abstract class ColorAnimation(@ColorRes private val initialColorRes: Int, @ColorRes private val finalColorRes: Int) : Animation() {

    private val evaluator = ArgbEvaluator()
    protected var initialColor: Int = 0
    protected var finalColor: Int = 0

    override fun init(vararg views: View) {
        val context = views[0].context

        initialColor = context.getColor(initialColorRes)
        finalColor = context.getColor(finalColorRes)
    }

    override fun updateAnimationByProportion(view: View, proportion: Float) {
        val color = evaluator.evaluate(proportion, initialColor, finalColor)
        update(view, color as Int)
    }

    abstract fun update(view: View, color: Int)
}

class ChangeTextColorAnimation(@ColorRes initialColorRes: Int, @ColorRes finalColorRes: Int) : ColorAnimation(initialColorRes, finalColorRes) {

    override fun clone(): Any {
        return ChangeTextColorAnimation(initialColor, finalColor).also {
            cloneFromTo(it, this)
        }
    }

    override fun update(view: View, color: Int) {
        (view as TextView?)?.setTextColor(color)
    }
}

class ChangeBackgroundAnimation(@ColorRes initialColorRes: Int, @ColorRes finalColorRes: Int) : ColorAnimation(initialColorRes, finalColorRes) {

    override fun clone(): Any {
        return ChangeBackgroundAnimation(initialColor, finalColor).also {
            cloneFromTo(it, this)
        }
    }

    override fun update(view: View, color: Int) {
        view.setBackgroundColor(color)
    }
}

open class ScaleAnimation(var scaleX: Float, var scaleY: Float) : Animation() {

    constructor(scale: Float) : this(scale, scale)

    var initialScaleX = 1f
    var initialScaleY = 1f
    protected var valueDeltaX = 0f
    protected var valueDeltaY = 0f

    override fun init(vararg views: View) {
        initialScaleX = views[0].scaleX
        initialScaleY = views[0].scaleY
        valueDeltaX = scaleX - initialScaleX
        valueDeltaY = scaleY - initialScaleY
    }

    override fun updateAnimationByProportion(view: View, proportion: Float) {
        val scaleX = initialScaleX + proportion * valueDeltaX
        val scaleY = initialScaleY + proportion * valueDeltaY
        view.scaleX = scaleX
        view.scaleY = scaleY
    }

    override fun clone(): Any {
        return ScaleAnimation(scaleX, scaleY).also {
            cloneFromTo(it, this)
        }
    }

    override fun addAnimation(view: View, animation: ViewPropertyAnimator) {
        animation
                .scaleX(scaleX)
                .scaleY(scaleY)
    }
}

class DirectionalScaleAnimation(scaleX: Float, scaleY: Float, var direction: Direction) : ScaleAnimation(scaleX, scaleY) {

    constructor(scale: Float, direction: Direction) : this(scale, scale, direction)

    private var initialHeight = 0
    private var deltaY: Float = 0.0f

    override fun init(vararg views: View) {
        super.init(*views)
        initialHeight = views[0].height
        //TODO: review this. this delta is necessary to handle previous scale operations
        deltaY = ((views[0].height * views[0].scaleY) - views[0].height) / 2
    }

    override fun updateAnimationByProportion(view: View, proportion: Float) {
        val scaleX = initialScaleX + proportion * valueDeltaX
        val yIncrement = proportion * valueDeltaY
        val scaleY = initialScaleY + yIncrement
        view.scaleX = scaleX
        view.scaleY = scaleY

        if (direction == Direction.Up) {
            val addedHeight = initialHeight * yIncrement
            val translation = addedHeight / 2 + deltaY
            view.translationY = -translation
        }

        if (direction == Direction.Down) {
            val addedHeight = initialHeight * yIncrement
            val translation = addedHeight / 2 + deltaY
            view.translationY = translation
        }

        //TODO: handle left and right directions
    }
}

class DelayAnimation(duration: Long) : Animation(duration) {
    override fun clone() = DelayAnimation(duration)

    override fun updateAnimationByProportion(view: View, proportion: Float) {
        //nothing to do
    }
}

class RotateAnimation(var angle: Float) : Animation() {

    private var initialRotation = 0f
    private var valueDelta = 0f

    override fun init(vararg views: View) {
        //TODO: add data to all views not only one
        initialRotation = views[0].rotation
        valueDelta = angle
        //TODO when implement rotateTo use:
        //valueDelta = angle - initialRotation
    }

    override fun updateAnimationByProportion(view: View, proportion: Float) {
        view.rotation = initialRotation + proportion * valueDelta
    }

    override fun clone(): Any {
        return RotateAnimation(angle).also {
            cloneFromTo(it, this)
        }
    }

    override fun addAnimation(view: View, animation: ViewPropertyAnimator) {
        animation.rotationBy(angle)
    }
}