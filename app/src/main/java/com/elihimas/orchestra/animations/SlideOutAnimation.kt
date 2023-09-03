package com.elihimas.orchestra.animations

import android.view.View

class SlideOutAnimation(direction: Direction) : SlideAnimation(direction) {
    override fun createSlideStrategy(): AnimationStrategy {
        return when (direction) {
            Direction.Up -> SlideOutUpStrategy()
            Direction.Down -> SlideOutDownStrategy()
            Direction.Left -> SlideOutLeftStrategy(remainingWidth)
            Direction.Right -> SlideOutRightStrategy()
        }
    }

    override fun afterAnimation(vararg views: View) {
        if (remainingWidth == 0f) {
            views.forEach { view -> view.visibility = View.INVISIBLE }
        }
    }
}