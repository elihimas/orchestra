package com.elihimas.orchestra.animations.scale

import android.view.View
import com.elihimas.orchestra.animations.Animation
import com.elihimas.orchestra.constrains.deeffectors.ScaleAnimationDeEffector

open class ScaleAnimation(var scaleX: Float, var scaleY: Float) : Animation() {

    var initialScaleX = 1f
    var initialScaleY = 1f
    private var valueDeltaX = 0f
    private var valueDeltaY = 0f

    override fun beforeAnimation(views: List<View>) {
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
        return ScaleAnimation(scaleX, scaleY).also { clone ->
            cloneFromTo(this, clone)
        }
    }

    override fun getDeEffector() = ScaleAnimationDeEffector()
}