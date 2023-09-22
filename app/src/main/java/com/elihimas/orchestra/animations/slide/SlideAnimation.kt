package com.elihimas.orchestra.animations.slide

import android.graphics.Rect
import android.view.View
import com.elihimas.orchestra.animations.AnimationStrategy
import com.elihimas.orchestra.animations.Direction
import com.elihimas.orchestra.animations.StatefulAnimation
import com.elihimas.orchestra.constrains.deeffectors.TranslationDeEffector
import kotlin.math.absoluteValue

// TODO: simplify horizontal strategies based on vertical slides
abstract class HorizontalSlideStrategy(
    val remainingWidth: Float, val startFromCurrentPosition: Boolean
) : AnimationStrategy<SlideData>{

}

abstract class VerticalSlideInStrategy(
    private val remainingHeight: Float,
    private val startFromCurrentPosition: Boolean
) : AnimationStrategy<SlideData> {

    override fun createAnimationDataFor(views: List<View>): List<SlideData> = buildList {
        views.forEach { view ->
            val initialVisibleHeight = if (startFromCurrentPosition) {
                view.height - view.translationY.absoluteValue
            } else {
                0f
            }

            add(SlideData(initialVisibleHeight))
        }
    }

    override fun update(view: View, proportion: Float, animationData: SlideData) {
        val visibleHeight =
            animationData.initialVisibleSpace + (view.height - remainingHeight - animationData.initialVisibleSpace) * proportion

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
    override fun createAnimationDataFor(views: List<View>): List<SlideData> = buildList {
        views.forEach { view ->
            val initialTranslationX = if (startFromCurrentPosition) {
                view.translationX
            } else {
                view.width.toFloat()
            }

            add(SlideData(initialTranslationX))
        }
    }

    override fun update(view: View, proportion: Float, animationData: SlideData) {
        val rightPush =
            view.width - animationData.initialVisibleSpace + (animationData.initialVisibleSpace - remainingWidth) * proportion
        val translationX = view.width - rightPush

        view.clipBounds = Rect(0, 0, rightPush.toInt(), view.height)
        view.translationX = translationX
    }
}

class SlideInRightStrategy(remainingWidth: Float, startFromCurrentPosition: Boolean) :
    HorizontalSlideStrategy(remainingWidth, startFromCurrentPosition) {
    override fun createAnimationDataFor(views: List<View>): List<SlideData> = buildList {
        views.forEach { view ->
            val initialTranslationX = if (startFromCurrentPosition) {
                view.translationX
            } else {
                -view.width.toFloat()
            }

            add(SlideData(initialTranslationX))
        }
    }

    override fun update(view: View, proportion: Float, animationData: SlideData) {
        val targetLeftPush = view.width - remainingWidth
        val leftPush =
            view.width + animationData.initialVisibleSpace + (targetLeftPush - (view.width + animationData.initialVisibleSpace)) * proportion

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
) : StatefulAnimation<SlideData>() {

    private lateinit var slideStrategy: AnimationStrategy<SlideData>

    open fun createSlideStrategy(): AnimationStrategy<SlideData> {
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

        animationDataList = slideStrategy.createAnimationDataFor(views)
    }

    override fun updateAnimationByProportion(
        view: View,
        proportion: Float,
        animationData: SlideData
    ) =
        slideStrategy.update(view, proportion, animationData)

    override fun getDeEffector() = TranslationDeEffector

    override fun clone(): Any {
        return SlideAnimation(direction, remainingSpace).also { clone ->
            cloneFromTo(this, clone)
        }
    }
}