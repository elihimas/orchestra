package com.elihimas.orchestra.animations

import android.view.View
import android.view.ViewPropertyAnimator
import androidx.core.view.isVisible

open class FadeInAnimation(var initialAlpha: Float = 0f, var finalAlpha: Float = 1f) : Animation(600) {

    private var valueDelta = 0f

    override fun beforeAnimation(views: List<View>) {
        valueDelta = finalAlpha - initialAlpha
        views.forEach { view -> view.isVisible = true }
    }

    override fun updateAnimationByProportion(view: View, proportion: Float) {
        view.alpha = initialAlpha + proportion * valueDelta
    }

    override fun clone(): Any {
        return FadeInAnimation(initialAlpha, finalAlpha).also { clone ->
            cloneFromTo(this, clone)
        }
    }

    override fun addAnimation(view: View, animation: ViewPropertyAnimator) {
        animation.alpha(finalAlpha)
    }
}