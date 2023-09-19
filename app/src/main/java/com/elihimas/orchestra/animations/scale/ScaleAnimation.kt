package com.elihimas.orchestra.animations.scale

import android.view.View
import com.elihimas.orchestra.animations.StatefulAnimation
import com.elihimas.orchestra.constrains.deeffectors.ScaleAnimationDeEffector

open class ScaleAnimation(var scaleX: Float, var scaleY: Float) : StatefulAnimation<ScaleData>() {

    override fun beforeAnimation(views: List<View>) {
        animationDataList = buildList {
            views.forEach { view ->
                val initialScaleX = view.scaleX
                val initialScaleY = view.scaleY
                val valueDeltaX = scaleX - initialScaleX
                val valueDeltaY = scaleY - initialScaleY

                add(ScaleData(initialScaleX, initialScaleY, valueDeltaX, valueDeltaY))
            }
        }
    }

    override fun updateAnimationByProportion(
        view: View,
        proportion: Float,
        animationData: ScaleData
    ) {
        val scaleX = animationData.initialScaleX + proportion * animationData.valueDeltaX
        val scaleY = animationData.initialScaleY + proportion * animationData.valueDeltaY
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