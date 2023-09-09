package com.elihimas.orchestra.animations

import android.graphics.Rect
import android.view.View

class SlideOutDownStrategy(remainingHeight: Float, startFromCurrentPosition: Boolean) :
    VerticalSlideStrategy(remainingHeight, startFromCurrentPosition) {

    override fun init(vararg views: View) {

    }

    override fun update(view: View, proportion: Float) {
        val down = view.height * proportion

        view.clipBounds = Rect(0, 0, view.width, (view.height - down).toInt())
        view.translationY = down + initialVisibleHeight
    }

    override fun updateVisibleHeight(view: View, visibleHeight: Float) {
        TODO("Not yet implemented")
    }
}

class SlideOutUpStrategy(remainingHeight: Float, startFromCurrentPosition: Boolean) :
    VerticalSlideStrategy(remainingHeight, startFromCurrentPosition) {
    override fun init(vararg views: View) {

    }

    override fun update(view: View, proportion: Float) {
        val top = view.height * proportion

        view.clipBounds = Rect(0, top.toInt(), view.width, view.height)
        view.translationY = -top + initialVisibleHeight
    }

    override fun updateVisibleHeight(view: View, visibleHeight: Float) {
        TODO("Not yet implemented")
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