package com.elihimas.orchestra.animations

import android.graphics.Rect
import android.view.View

abstract class HorizontalSlideStrategy : AnimationStrategy {

    var initialTranslationX = 0f

    override fun init(vararg views: View) {
        initialTranslationX = views[0].translationX
    }
}

abstract class VerticalSlideStrategy : AnimationStrategy {

    var initialTranslationY = 0f

    override fun init(vararg views: View) {
        initialTranslationY = views[0].translationY
    }
}

class SlideUpReverseStrategy : VerticalSlideStrategy() {

    override fun update(view: View, proportion: Float) {
        val down = view.height * proportion

        view.clipBounds = Rect(0, 0, view.width, (view.height - down).toInt())
        view.translationY = down + initialTranslationY
    }
}

class SlideDownReverseStrategy : VerticalSlideStrategy() {

    override fun update(view: View, proportion: Float) {
        val top = view.height * proportion

        view.clipBounds = Rect(0, top.toInt(), view.width, view.height)
        view.translationY = -top + initialTranslationY
    }
}

class SlideLeftReverseStrategy : HorizontalSlideStrategy() {

    override fun update(view: View, proportion: Float) {
        val right = view.width * proportion

        view.clipBounds = Rect(0, 0, (view.width - right).toInt(), view.height)
        view.translationX = right + initialTranslationX
    }
}

class SlideRightReverseStrategy : HorizontalSlideStrategy() {

    override fun update(view: View, proportion: Float) {
        val left = view.width * proportion

        view.clipBounds = Rect(left.toInt(), 0, view.width, view.height)
        view.translationX = -left + initialTranslationX
    }
}

class SlideUpStrategy : VerticalSlideStrategy() {

    override fun update(view: View, proportion: Float) {
        val down = view.height * proportion

        view.clipBounds = Rect(0, 0, view.width, down.toInt())
        view.translationY = view.height - down + initialTranslationY
    }
}

class SlideDownStrategy : VerticalSlideStrategy() {

    override fun update(view: View, proportion: Float) {
        val top = view.height * proportion

        view.clipBounds = Rect(0, view.height - top.toInt(), view.width, view.height)
        view.translationY = -view.height + top + initialTranslationY
    }

}

class SlideLeftStrategy : HorizontalSlideStrategy() {

    override fun update(view: View, proportion: Float) {
        val right = view.width * proportion

        view.clipBounds = Rect(0, 0, right.toInt(), view.height)
        view.translationX = view.width - right + initialTranslationX
    }
}

class SlideRightStrategy : HorizontalSlideStrategy() {

    override fun update(view: View, proportion: Float) {
        val left = view.width * proportion

        view.clipBounds = Rect(view.width - left.toInt(), 0, view.width, view.height)
        view.translationX = -view.width + left + initialTranslationX
    }
}

/**
 * Slide in and out animation implementation
 *
 * @param direction the direction towards to the view should be animated
 * @param reverseAnimation if the animation is a slide out animation
 */
open class SlideAnimation(internal var direction: Direction, private val reverseAnimation: Boolean = false) : Animation() {

    private val slideStrategy: AnimationStrategy

    init {
        val actualDirection = if (reverseAnimation) {
            direction.reverse()
        } else {
            direction
        }

        slideStrategy = if (reverseAnimation) {
            when (actualDirection) {
                Direction.Up -> SlideUpReverseStrategy()
                Direction.Down -> SlideDownReverseStrategy()
                Direction.Left -> SlideLeftReverseStrategy()
                Direction.Right -> SlideRightReverseStrategy()
            }
        } else {
            when (actualDirection) {
                Direction.Up -> SlideUpStrategy()
                Direction.Down -> SlideDownStrategy()
                Direction.Left -> SlideLeftStrategy()
                Direction.Right -> SlideRightStrategy()
            }
        }
    }

    override fun clone(): Any {
        return SlideAnimation(direction, reverseAnimation).also {clone ->
            cloneFromTo(this, clone)
        }
    }

    //TODO create variables for each view
    override fun init(vararg views: View) {
        views.forEach { view -> view.visibility = View.VISIBLE }

        slideStrategy.init(*views)
    }

    override fun updateAnimationByProportion(view: View, proportion: Float) = slideStrategy.update(view, proportion)
}