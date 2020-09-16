package com.elihimas.orchestra

import android.view.View
import android.view.ViewGroup

abstract class SetupAction {
    fun runSetup(views: Array<out View>) {
        views.forEach {
            runSetup(it)
        }
    }

    abstract fun runSetup(view: View)
}

class AlphaAction(private val value: Float) : SetupAction() {
    override fun runSetup(view: View) {
        view.alpha = value
    }
}

class SlideHideAction(private val direction: Direction) : SetupAction() {
    override fun runSetup(view: View) {
        view.apply {

            measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED))

            val width = measuredWidth
            val height = measuredHeight

            layoutParams = (layoutParams as ViewGroup.MarginLayoutParams).also {
                when (direction) {
                    Direction.Up -> {
                        it.topMargin += height
                        it.height = 0
                    }
                    Direction.Down -> {
                        it.bottomMargin += height
                        it.height = 0
                    }
                    Direction.Start -> {
                        it.marginEnd += width
                        it.width = 0
                    }
                    Direction.End -> {
                        it.marginStart += width
                        it.width = 0
                    }
                }
            }
        }
    }

}

class SetupScaleAction(private val value: Float) : SetupAction() {
    override fun runSetup(view: View) {
        view.apply {
            scaleX = value
            scaleY = value
        }
    }
}

class CircularRevealHideAction() : SetupAction() {
    override fun runSetup(view: View) {
        view.visibility = View.INVISIBLE
    }

}
