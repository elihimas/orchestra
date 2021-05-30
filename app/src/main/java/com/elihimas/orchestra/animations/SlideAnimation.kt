package com.elihimas.orchestra.animations

import android.animation.ValueAnimator
import android.graphics.Rect
import android.view.View
import androidx.core.animation.doOnEnd

//TODO add strategy to simplify code5
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