package com.elihimas.orchestra.animations.slide

import android.graphics.Rect
import android.view.View
import com.elihimas.orchestra.animations.AnimationStrategy
import com.elihimas.orchestra.animations.Direction
import kotlin.math.absoluteValue

abstract class VerticalSlideOutStrategy(
    private val remainingHeight: Float,
    private val startFromCurrentPosition: Boolean
) : AnimationStrategy {

    private var initialPush = 0f

    override fun init(vararg views: View) {
        initialPush = if (startFromCurrentPosition) {
             views[0].translationY.absoluteValue
        } else {
            0f
        }
    }

    override fun update(view: View, proportion: Float) {
        val targetPush = view.height - remainingHeight
        val push =
            initialPush + (targetPush - initialPush) * (proportion)

        updatePush(view, push)
    }

    abstract fun updatePush(view: View, push: Float)
}

class SlideOutDownStrategy(remainingHeight: Float, startFromCurrentPosition: Boolean) :
    VerticalSlideOutStrategy(remainingHeight, startFromCurrentPosition) {

    override fun updatePush(view: View, push: Float) {
        view.clipBounds = Rect(0, 0, view.width, view.height - push.toInt())
        view.translationY = push
    }
}

class SlideOutUpStrategy(remainingHeight: Float, startFromCurrentPosition: Boolean) :
    VerticalSlideOutStrategy(remainingHeight, startFromCurrentPosition) {

    override fun updatePush(view: View, push: Float) {
        view.clipBounds = Rect(0, push.toInt(), view.width, view.height)
        view.translationY = -push
    }
}

class SlideOutRightStrategy(remainingWidth: Float, startFromCurrentPosition: Boolean) :
    HorizontalSlideStrategy(
        remainingWidth,
        startFromCurrentPosition
    ) {

    override fun init(vararg views: View) {
        initialTranslationX = if (startFromCurrentPosition) {
            views[0].translationX
        } else {
            0f
        }
    }

    override fun update(view: View, proportion: Float) {
        val targetTranslation = view.width - remainingWidth
        val rightPush = initialTranslationX + (targetTranslation - initialTranslationX) * proportion

        view.clipBounds = Rect(0, 0, (view.width - rightPush).toInt(), view.height)
        view.translationX = rightPush
    }
}

class SlideOutLeftStrategy(remainingWidth: Float, startFromCurrentPosition: Boolean) :
    HorizontalSlideStrategy(
        remainingWidth,
        startFromCurrentPosition
    ) {

    override fun init(vararg views: View) {
        initialTranslationX = if (startFromCurrentPosition) {
            -views[0].translationX
        } else {
            0f
        }
    }

    var initial = 0f
    override fun update(view: View, proportion: Float) {
        val targetTranslation = view.width - remainingWidth
        val leftPush = initialTranslationX + (targetTranslation - initialTranslationX) * proportion

        view.clipBounds = Rect(leftPush.toInt(), 0, view.width, view.height)
        view.translationX = -leftPush

        if (proportion == 0f) {
            initial = leftPush
        }
        if (proportion == 1f) {
            println(leftPush)
        }
    }
}

class SlideOutAnimation(direction: Direction) : SlideAnimation(direction) {
    override fun createSlideStrategy(): AnimationStrategy {
        return when (direction) {
            Direction.Up -> SlideOutUpStrategy(remainingSpace, startFromCurrentPosition)
            Direction.Down -> SlideOutDownStrategy(remainingSpace, startFromCurrentPosition)
            Direction.Left -> SlideOutLeftStrategy(remainingSpace, startFromCurrentPosition)
            Direction.Right -> SlideOutRightStrategy(remainingSpace, startFromCurrentPosition)
        }
    }

    override fun afterAnimation(vararg views: View) {
        if (remainingSpace == 0f) {
            views.forEach { view -> view.visibility = View.INVISIBLE }
        }
    }
}