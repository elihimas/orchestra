package com.elihimas.orchestra

import android.animation.Animator
import android.animation.ValueAnimator
import android.graphics.Rect
import android.view.*
import android.view.animation.Animation
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

private fun Animation.setEndListener(block: (Animation?) -> Unit?) {
    setAnimationListener(object : Animation.AnimationListener {
        override fun onAnimationStart(animation: Animation?) {

        }

        override fun onAnimationRepeat(animation: Animation?) {
        }

        override fun onAnimationEnd(animation: Animation?) {
            block(animation)
        }

    })
}

private fun Animator.addEndListener(block: (Animator?) -> Unit?) {
    addListener(object : Animator.AnimatorListener {
        override fun onAnimationStart(animation: Animator?) {
        }

        override fun onAnimationEnd(animation: Animator?) {
            block(animation)
        }

        override fun onAnimationCancel(animation: Animator?) {
        }

        override fun onAnimationRepeat(animation: Animator?) {
        }

    })
}

abstract class Action(var duration: Long = 2600L, var spacing: Long = 0) {
    open fun runAnimation(view: View, endAction: Runnable?) {
        beforeAnimation(view)
        addAnimation(view, view.animate().also {
            it.withEndAction(endAction)
                    .setDuration(duration)
                    .setListener(null)
        })
    }

    open fun beforeAnimation(view: View) {}

    abstract fun addAnimation(view: View, animation: ViewPropertyAnimator)
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

        anim.addEndListener {
            endAction?.run()
        }
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

            addEndListener {
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


    override fun addAnimation(view: View, animation: ViewPropertyAnimator) {

        //nothing to do
    }
}


class ParallelActions(private val reference: Animations) : Action() {

    override fun addAnimation(view: View, animation: ViewPropertyAnimator) {
        reference.actions.forEach { action ->
            action.addAnimation(view, animation)
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

    override fun addAnimation(view: View, animation: ViewPropertyAnimator) {
        //nothing to do
    }
}

class TranslateAction(private val x: Float, private val y: Float) : Action() {

    override fun addAnimation(view: View, animation: ViewPropertyAnimator) {
        animation
                .translationX(x)
                .translationY(y)
    }
}

class CropShowAction : Action() {

    override fun addAnimation(view: View, animation: ViewPropertyAnimator) {
        animation.setUpdateListener { animator ->
            view.layoutParams.let {
                it.width = (animator.animatedFraction * 100).toInt()
                view.layoutParams = it
            }
        }
    }
}


class ScaleAction(private val scale: Float) : Action() {
    override fun beforeAnimation(view: View) {
        view.scaleX
    }

    override fun addAnimation(view: View, animation: ViewPropertyAnimator) {
        animation
                .scaleX(scale)
                .scaleY(scale)
    }

}