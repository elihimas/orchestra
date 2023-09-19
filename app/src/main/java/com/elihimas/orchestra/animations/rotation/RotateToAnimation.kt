package com.elihimas.orchestra.animations.rotation

import android.view.View
import com.elihimas.orchestra.animations.StatefulAnimation

class RotateToAnimation(var angle: Float) : StatefulAnimation<RotateToData>() {


    override fun beforeAnimation(views: List<View>) {
        animationDataList = buildList {
            views.forEach { view ->
                val initialRotation = view.rotation
                val valueDelta = angle - initialRotation

                add(RotateToData(initialRotation, valueDelta))
            }
        }
    }

    override fun updateAnimationByProportion(
        view: View,
        proportion: Float,
        animationData: RotateToData
    ) {
        view.rotation = animationData.initialRotation + proportion * animationData.valueDelta
    }

    override fun clone(): Any {
        return RotateToAnimation(angle).also { clone ->
            cloneFromTo(this, clone)
        }
    }
}