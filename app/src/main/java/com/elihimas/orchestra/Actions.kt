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
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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

abstract class Action(var duration: Long = OrchestraConfiguration.General.duration,
                      var spacing: Long = OrchestraConfiguration.General.spacing) {
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
}

class CircularRevealAction : Action() {
    override fun runAnimation(view: View, endAction: Runnable?) {
        val cx = view.width / 2
        val cy = view.height / 2

        // get the final radius for the clipping circle
        val finalRadius = Math.hypot(cx.toDouble(), cy.toDouble()).toFloat()

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
class SlideAction(private val direction: Direction, private val reverseAnimation: Boolean = false) : Action() {
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

            this.duration = this@SlideAction.duration
        }
        animator.start()
    }

}

class ParallelActions(private val reference: Animations) : Action() {

    override fun addAnimation(view: View, animation: ViewPropertyAnimator) {
    }

    override fun runAnimation(view: View, endAction: Runnable?) {
        reference.actions.forEach { action ->
            action.runAnimation(view, endAction)
        }
    }
}

class DelayAction(duration: Long) : Action(duration) {
    override fun runAnimation(view: View, endAction: Runnable?) {
        GlobalScope.launch {
            delay(duration)
            endAction?.run()
        }
    }
}

class TranslateAction(private val x: Float, private val y: Float) : Action() {

    override fun addAnimation(view: View, animation: ViewPropertyAnimator) {
        animation
                .translationX(x)
                .translationY(y)
    }
}

abstract class AnimateColorAction(@ColorRes vararg val colorResIds: Int) : Action() {
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
            duration = this@AnimateColorAction.duration

            addUpdateListener(createUpdateListener(view))
            doOnEnd { endAction?.run() }
            start()
        }
    }

    abstract fun createUpdateListener(view: View): ValueAnimator.AnimatorUpdateListener
}

class ChangeTextColorAction(@ColorRes vararg colorResIds: Int) : AnimateColorAction(*colorResIds) {
    override fun createUpdateListener(view: View): ValueAnimator.AnimatorUpdateListener {
        return ValueAnimator.AnimatorUpdateListener {
            (view as TextView?)?.setTextColor(it.animatedValue as Int)
        }
    }
}

class ChangeBackgroundAction(@ColorRes vararg colorResIds: Int) : AnimateColorAction(*colorResIds) {
    override fun createUpdateListener(view: View): ValueAnimator.AnimatorUpdateListener {
        return ValueAnimator.AnimatorUpdateListener {
            (view as TextView).setBackgroundColor(it.animatedValue as Int)
        }
    }
}

class ScaleAction(private val scale: Float) : Action() {

    override fun addAnimation(view: View, animation: ViewPropertyAnimator) {
        animation
                .scaleX(scale)
                .scaleY(scale)
    }

}