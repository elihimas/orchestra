package com.elihimas.orchestra.animations

import android.graphics.Rect
import android.view.View
import com.elihimas.orchestra.constrains.deeffectors.TranslationDeEffector

abstract class HorizontalSlideStrategy(
    val remainingWidth: Float, val startFromCurrentPosition: Boolean
) : AnimationStrategy {

    var initialTranslationX = 0f
}

abstract class VerticalSlideStrategy : AnimationStrategy {

    var initialTranslationY = 0f

    override fun init(vararg views: View) {
        // TODO: verify if this makes sense in some scenario
        // initialTranslationY = views[0].translationY
    }
}

class SlideInUpStrategy : VerticalSlideStrategy() {

    override fun update(view: View, proportion: Float) {
        val down = view.height * proportion

        view.clipBounds = Rect(0, 0, view.width, down.toInt())
        view.translationY = view.height - down + initialTranslationY
    }
}

class SlideInDownStrategy : VerticalSlideStrategy() {

    override fun update(view: View, proportion: Float) {
        val top = view.height * proportion

        view.clipBounds = Rect(0, view.height - top.toInt(), view.width, view.height)
        view.translationY = -view.height + top + initialTranslationY
    }

}

class SlideInLeftStrategy(remainingWidth: Float, startFromCurrentPosition: Boolean) :
    HorizontalSlideStrategy(remainingWidth, startFromCurrentPosition) {
    override fun init(vararg views: View) {
        initialTranslationX = if (startFromCurrentPosition) {
            views[0].translationX
        } else {
            views[0].width.toFloat()
        }
    }

    override fun update(view: View, proportion: Float) {
        val rightPush =
            view.width - initialTranslationX + (initialTranslationX - remainingWidth) * proportion
        val translationX = view.width - rightPush

        view.clipBounds = Rect(0, 0, rightPush.toInt(), view.height)
        view.translationX = translationX
    }
}

class SlideInRightStrategy(remainingWidth: Float, startFromCurrentPosition: Boolean) :
    HorizontalSlideStrategy(remainingWidth, startFromCurrentPosition) {
    override fun init(vararg views: View) {
        initialTranslationX = if (startFromCurrentPosition) {
            views[0].translationX
        } else {
            -views[0].width.toFloat()
        }
    }

    override fun update(view: View, proportion: Float) {
        val targetLeftPush = view.width - remainingWidth
        val leftPush =
            view.width + initialTranslationX + (targetLeftPush - (view.width + initialTranslationX)) * proportion

        view.clipBounds = Rect(view.width - (leftPush.toInt()), 0, view.width, view.height)
        view.translationX = -view.width + leftPush
    }
}

/**
 * Slide in and out animation implementation
 *
 * @param direction the direction towards to the view should be animated
 */
open class SlideAnimation(
    protected var direction: Direction,
    var remainingWidth: Float = 0f,
    var startFromCurrentPosition: Boolean = false
) : Animation() {

    private lateinit var slideStrategy: AnimationStrategy

    open fun createSlideStrategy(): AnimationStrategy {
        return when (direction) {
            Direction.Up -> SlideInUpStrategy()
            Direction.Down -> SlideInDownStrategy()
            Direction.Left -> SlideInLeftStrategy(remainingWidth, startFromCurrentPosition)
            Direction.Right -> SlideInRightStrategy(remainingWidth, startFromCurrentPosition)
        }
    }

    override fun beforeAnimation(vararg views: View) {
        slideStrategy = createSlideStrategy()

        views.forEach { view -> view.visibility = View.VISIBLE }

        slideStrategy.init(*views)
    }

    override fun updateAnimationByProportion(view: View, proportion: Float) =
        slideStrategy.update(view, proportion)

    override fun getDeEffector() = TranslationDeEffector

    override fun clone(): Any {
        return SlideAnimation(direction, remainingWidth).also { clone ->
            cloneFromTo(this, clone)
        }
    }
}