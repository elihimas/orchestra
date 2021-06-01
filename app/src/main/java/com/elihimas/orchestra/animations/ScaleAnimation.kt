package com.elihimas.orchestra.animations

import android.view.View

open class ScaleAnimation(var scaleX: Float, var scaleY: Float) : Animation() {

    constructor(scale: Float) : this(scale, scale)

    var initialScaleX = 1f
    var initialScaleY = 1f
    private var valueDeltaX = 0f
    private var valueDeltaY = 0f

    override fun init(vararg views: View) {
        initialScaleX = views[0].scaleX
        initialScaleY = views[0].scaleY
        valueDeltaX = scaleX - initialScaleX
        valueDeltaY = scaleY - initialScaleY
    }

    override fun updateAnimationByProportion(view: View, proportion: Float) {
        val scaleX = initialScaleX + proportion * valueDeltaX
        val scaleY = initialScaleY + proportion * valueDeltaY
        view.scaleX = scaleX
        view.scaleY = scaleY
    }

    override fun clone(): Any {
        return ScaleAnimation(scaleX, scaleY).also {
            cloneFromTo(it, this)
        }
    }
}