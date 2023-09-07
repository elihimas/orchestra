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

class SlideOutRightStrategy(private val remainingWidth: Float) : HorizontalSlideStrategy() {

    override fun update(view: View, proportion: Float) {
        val rightPush = initialTranslationX + (view.width - remainingWidth - initialTranslationX) * proportion

        view.clipBounds = Rect(0, 0, (view.width - rightPush).toInt(), view.height)
        view.translationX = rightPush
    }
}

class SlideOutLeftStrategy(private val remainingWidth: Float) : HorizontalSlideStrategy() {

    override fun update(view: View, proportion: Float) {
        val left = view.width * proportion
        val remainingWidthProportion = remainingWidth * proportion

        view.clipBounds =
            Rect((left - remainingWidthProportion).toInt(), 0, view.width, view.height)
        view.translationX = -left + remainingWidthProportion
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