package com.elihimas.orchestra.animations

import android.view.View
import android.view.ViewPropertyAnimator

class RotateAnimation(var angle: Float) : Animation() {

    private var initialRotation = 0f
    private var valueDelta = 0f

    override fun init(vararg views: View) {
        //TODO: add data to all views not only one
        initialRotation = views[0].rotation
        valueDelta = angle
        //TODO when implement rotateTo use:
        //valueDelta = angle - initialRotation
    }

    override fun updateAnimationByProportion(view: View, proportion: Float) {
        view.rotation = initialRotation + proportion * valueDelta
    }

    override fun clone(): Any {
        return RotateAnimation(angle).also {
            cloneFromTo(it, this)
        }
    }

    override fun addAnimation(view: View, animation: ViewPropertyAnimator) {
        animation.rotationBy(angle)
    }
}