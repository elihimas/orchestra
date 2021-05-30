package com.elihimas.orchestra.animations

import android.view.View
import android.view.ViewPropertyAnimator

class TranslateAnimation(internal val x: Float, internal val y: Float) : Animation() {

    private var initialX = 0f
    private var initialY = 0f

    override fun init(vararg views: View) {
        initialX = views[0].translationX
        initialY = views[0].translationY
    }

    override fun updateAnimationByProportion(view: View, proportion: Float) {
        val newX = initialX + proportion * x
        val newY = initialY + proportion * y

        view.translationX = newX
        view.translationY = newY
    }

    override fun clone(): Any {
        return TranslateAnimation(x, y).also {
            cloneFromTo(it, this)
        }
    }

    override fun addAnimation(view: View, animation: ViewPropertyAnimator) {
        animation
                .translationX(x)
                .translationY(y)
    }
}