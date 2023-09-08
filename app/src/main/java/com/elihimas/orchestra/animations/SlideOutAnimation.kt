package com.elihimas.orchestra.animations

import android.graphics.Rect
import android.view.View

class SlideOutDownStrategy : VerticalSlideStrategy() {

    override fun update(view: View, proportion: Float) {
        val down = view.height * proportion

        view.clipBounds = Rect(0, 0, view.width, (view.height - down).toInt())
        view.translationY = down + initialTranslationY
    }
}

class SlideOutUpStrategy : VerticalSlideStrategy() {

    override fun update(view: View, proportion: Float) {
        val top = view.height * proportion

        view.clipBounds = Rect(0, top.toInt(), view.width, view.height)
        view.translationY = -top + initialTranslationY
    }
}

class SlideOutRightStrategy(remainingWidth: Float) : HorizontalSlideStrategy(remainingWidth) {

    override fun init(vararg views: View) {
        initialTranslationX = views[0].translationX
    }

    override fun update(view: View, proportion: Float) {
        val targetTranslation = view.width - remainingWidth
        val rightPush = initialTranslationX + (targetTranslation - initialTranslationX) * proportion

        view.clipBounds = Rect(0, 0, (view.width - rightPush).toInt(), view.height)
        view.translationX = rightPush
    }
}

class SlideOutLeftStrategy(remainingWidth: Float) : HorizontalSlideStrategy(remainingWidth) {

    override fun init(vararg views: View) {
        initialTranslationX = -views[0].translationX
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
            Direction.Up -> SlideOutUpStrategy()
            Direction.Down -> SlideOutDownStrategy()
            Direction.Left -> SlideOutLeftStrategy(remainingWidth)
            Direction.Right -> SlideOutRightStrategy(remainingWidth)
        }
    }

    override fun afterAnimation(vararg views: View) {
        if (remainingWidth == 0f) {
            views.forEach { view -> view.visibility = View.INVISIBLE }
        }
    }
}