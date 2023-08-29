package com.elihimas.orchestra.animations

import android.view.View

class RotateAnimation(var angle: Float) : Animation() {

    private var initialRotation = 0f
    private var valueDelta = 0f

    override fun beforeAnimation(vararg views: View) {
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
        return RotateAnimation(angle).also { clone ->
            cloneFromTo(this, clone)
        }
    }
}