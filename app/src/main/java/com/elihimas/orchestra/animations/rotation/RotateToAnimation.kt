package com.elihimas.orchestra.animations.rotation

import android.view.View
import com.elihimas.orchestra.animations.Animation

class RotateToAnimation(var angle: Float) : Animation() {

    private var initialRotation = 0f
    private var valueDelta = 0f

    override fun beforeAnimation(views: List<View>) {
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