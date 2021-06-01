package com.elihimas.orchestra.animations

import android.view.View
import android.view.ViewPropertyAnimator

open class FadeInAnimation(var initialAlpha: Float = 0f, var finalAlpha: Float = 1f) : Animation(600) {

    private var valueDelta = 0f

    override fun init(vararg views: View) {
        valueDelta = finalAlpha - initialAlpha
    }

    override fun updateAnimationByProportion(view: View, proportion: Float) {
        view.alpha = initialAlpha + proportion * valueDelta
    }

    override fun clone(): Any {
        return FadeInAnimation(initialAlpha, finalAlpha).also { clone ->
            cloneFromTo(this, clone)
        }
    }

    override fun beforeAnimation(view: View) {
        view.alpha = initialAlpha
    }

    override fun addAnimation(view: View, animation: ViewPropertyAnimator) {
        animation.alpha(finalAlpha)
    }
}