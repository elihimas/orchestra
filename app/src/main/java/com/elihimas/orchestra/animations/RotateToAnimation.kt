package com.elihimas.orchestra.animations

import android.view.View

class RotateToAnimation(var angle: Float) : Animation() {

    private var initialRotation = 0f
    private var valueDelta = 0f

    override fun beforeAnimation(vararg views: View) {
        initialRotation = views[0].rotation
        valueDelta = angle - initialRotation
    }

    override fun updateAnimationByProportion(view: View, proportion: Float) {
        view.rotation = initialRotation + proportion * valueDelta
    }

    override fun clone(): Any {
        return RotateToAnimation(angle).also { clone ->
            cloneFromTo(this, clone)
        }
    }
}