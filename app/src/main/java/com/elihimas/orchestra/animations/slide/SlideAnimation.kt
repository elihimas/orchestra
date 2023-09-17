package com.elihimas.orchestra.animations.slide

import android.graphics.Rect
import android.view.View
import com.elihimas.orchestra.animations.Animation
import com.elihimas.orchestra.animations.AnimationStrategy
import com.elihimas.orchestra.animations.Direction
import com.elihimas.orchestra.constrains.deeffectors.TranslationDeEffector
import kotlin.math.absoluteValue

// TODO: simplify horizontal strategies based on vertical slides
abstract class HorizontalSlideStrategy(
    val remainingWidth: Float, val startFromCurrentPosition: Boolean
) : AnimationStrategy {

    var initialTranslationX = 0f
}

abstract class VerticalSlideInStrategy(
    private val remainingHeight: Float,
    private val startFromCurrentPosition: Boolean
) : AnimationStrategy {

    var initialVisibleHeight = 0f

    override fun init(views: List<View>) {
        initialVisibleHeight = if (startFromCurrentPosition) {
            views[0].height - views[0].translationY.absoluteValue
        } else {
            0f
        }
    }

    override fun update(view: View, proportion: Float) {
        val visibleHeight =
            initialVisibleHeight + (view.height - remainingHeight - initialVisibleHeight) * proportion

        updateVisibleHeight(view, visibleHeight)
    }

    abstract fun updateVisibleHeight(view: View, visibleHeight: Float)
}

class SlideInUpStrategy(remainingHeight: Float, startFromCurrentPosition: Boolean) :
    VerticalSlideInStrategy(remainingHeight, startFromCurrentPosition) {

    override fun updateVisibleHeight(view: View, visibleHeight: Float) {
        view.clipBounds = Rect(0, 0, view.width, visibleHeight.toInt())
        view.translationY = view.height - visibleHeight
    }
}

class SlideInDownStrategy(remainingHeight: Float, startFromCurrentPosition: Boolean) :
    VerticalSlideInStrategy(remainingHeight, startFromCurrentPosition) {

    override fun updateVisibleHeight(view: View, visibleHeight: Float) {
        view.clipBounds = Rect(0, view.height - visibleHeight.toInt(), view.width, view.height)
        view.translationY = visibleHeight - view.height
    }

}

class SlideInLeftStrategy(remainingWidth: Float, startFromCurrentPosition: Boolean) :
    HorizontalSlideStrategy(remainingWidth, startFromCurrentPosition) {
    override fun init(views: List<View>) {
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
    override fun init(views: List<View>) {
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
    var remainingSpace: Float = 0f,
    var startFromCurrentPosition: Boolean = false
) : Animation() {

    private lateinit var slideStrategy: AnimationStrategy

    open fun createSlideStrategy(): AnimationStrategy {
        return when (direction) {
            Direction.Up -> SlideInUpStrategy(remainingSpace, startFromCurrentPosition)
            Direction.Down -> SlideInDownStrategy(remainingSpace, startFromCurrentPosition)
            Direction.Left -> SlideInLeftStrategy(remainingSpace, startFromCurrentPosition)
            Direction.Right -> SlideInRightStrategy(remainingSpace, startFromCurrentPosition)
        }
    }

    override fun beforeAnimation(views: List<View>) {
        slideStrategy = createSlideStrategy()

        views.forEach { view -> view.visibility = View.VISIBLE }

        slideStrategy.init(views)
    }

    override fun updateAnimationByProportion(view: View, proportion: Float) =
        slideStrategy.update(view, proportion)

    override fun getDeEffector() = TranslationDeEffector

    override fun clone(): Any {
        return SlideAnimation(direction, remainingSpace).also { clone ->
            cloneFromTo(this, clone)
        }
    }
}