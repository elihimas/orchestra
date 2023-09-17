package com.elihimas.orchestra.animations

import android.view.View

class RotateByAnimation(var angle: Float) : Animation() {

    private var initialRotation = 0f
    private var valueDelta = 0f

    override fun beforeAnimation(views: List<View>) {
        initialRotation = views[0].rotation
        valueDelta = angle
    }

    override fun updateAnimationByProportion(view: View, proportion: Float) {
        view.rotation = initialRotation + proportion * valueDelta
    }

    override fun clone(): Any {
        return RotateByAnimation(angle).also { clone ->
            cloneFromTo(this, clone)
        }
    }
}