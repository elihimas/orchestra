package com.elihimas.orchestra

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewPropertyAnimator
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.core.animation.doOnEnd
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
                         var spacing: Long = OrchestraConfiguration.General.spacing) : Cloneable {
    var start = 0F
        set(value) {
            delta = end - value
            field = value
        }
    var end = 0f
        set(value) {
            delta = value - start
            field = value
        }

    var delta = 0f

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

    open fun updateAnimation(view: View, proportion: Float) {
        TODO("Not yet implemented")
    }

    open fun init(vararg views: View) {}
}

open class FadeInAnimation(var initialAlpha: Float = 0f, var finalAlpha: Float = 1f) : Animation(600) {

    private val valueDelta = finalAlpha - initialAlpha

    override fun updateAnimation(view: View, proportion: Float) {
        view.alpha = initialAlpha + proportion * (valueDelta)
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

class CircularRevealAnimation : Animation() {

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

    override fun clone(): Any {
        return SlideAnimation(direction, reverseAnimation).also {
            cloneFromTo(it, this)
        }
    }

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

class ParallelAnimation(private val reference: Animations) : Animation() {

    override fun clone(): Any {
        return ParallelAnimation(reference).also {
            cloneFromTo(it, this)
        }
    }

    override fun addAnimation(view: View, animation: ViewPropertyAnimator) {
    }

    override fun runAnimation(view: View, endAction: Runnable?) {
        reference.animations.forEach { action ->
            action.runAnimation(view, endAction)
        }
    }
}

class TranslateAnimation(private val x: Float, private val y: Float) : Animation() {

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

abstract class ColorAnimation(@ColorRes vararg val colorResIds: Int) : Animation() {

    override fun runAnimation(view: View, endAction: Runnable?) {
        val resources = view.context.resources
        val colorTo = colorResIds.map { resources.getColor(it, view.context.theme) }.toTypedArray()
        val colorAnimation =
                if (colorTo.size == 1) {
                    val firstColor: Int = with(view.background) {
                        if (this is ColorDrawable) {
                            this.color
                        } else {
                            resources.getColor(android.R.color.transparent, view.context.theme)
                        }
                    }
                    ValueAnimator.ofObject(ArgbEvaluator(), firstColor, colorTo[0])
                } else {
                    ValueAnimator.ofObject(ArgbEvaluator(), *colorTo)
                }

        with(colorAnimation) {
            duration = this@ColorAnimation.duration

            addUpdateListener(createUpdateListener(view))
            doOnEnd { endAction?.run() }
            start()
        }
    }

    abstract fun createUpdateListener(view: View): ValueAnimator.AnimatorUpdateListener
}

class ChangeTextColorAnimation(@ColorRes vararg colorResIds: Int) : ColorAnimation(*colorResIds) {

    override fun clone(): Any {
        return ChangeTextColorAnimation(*colorResIds).also {
            cloneFromTo(it, this)
        }
    }

    override fun createUpdateListener(view: View): ValueAnimator.AnimatorUpdateListener {
        return ValueAnimator.AnimatorUpdateListener {
            (view as TextView?)?.setTextColor(it.animatedValue as Int)
        }
    }
}

class ChangeBackgroundAnimation(@ColorRes vararg colorResIds: Int) : ColorAnimation(*colorResIds) {

    override fun clone(): Any {
        return ChangeBackgroundAnimation(*colorResIds).also {
            cloneFromTo(it, this)
        }
    }

    override fun createUpdateListener(view: View): ValueAnimator.AnimatorUpdateListener {
        return ValueAnimator.AnimatorUpdateListener {
            view.setBackgroundColor(it.animatedValue as Int)
        }
    }
}

class ScaleAnimation(var scale: Float) : Animation() {

    private var initialScale = 1f
    private var valueDelta = 0f

    override fun init(vararg views: View) {
        initialScale = views[0].scaleX
        valueDelta = scale - initialScale
    }

    override fun updateAnimation(view: View, proportion: Float) {
        val scale = initialScale + proportion * valueDelta
        view.scaleX = scale
        view.scaleY = scale
    }

    override fun clone(): Any {
        return ScaleAnimation(scale).also {
            cloneFromTo(it, this)
        }
    }

    override fun addAnimation(view: View, animation: ViewPropertyAnimator) {
        animation
                .scaleX(scale)
                .scaleY(scale)
    }

}

class RotateAnimation(var angle: Float) : Animation() {

    private var initialRotation = 0f
    private var valueDelta = 0f

    override fun init(vararg views: View) {
        initialRotation = views[0].rotation
        valueDelta = angle
        //TODO when implement rotateTo use:
        //valueDelta = angle - initialRotation
    }

    override fun updateAnimation(view: View, proportion: Float) {
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