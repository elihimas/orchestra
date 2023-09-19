package com.elihimas.orchestra.animations.rotation

import android.view.View
import com.elihimas.orchestra.animations.StatefulAnimation

class RotateByAnimation(private var angle: Float) : StatefulAnimation<RotateByData>() {

    override fun beforeAnimation(views: List<View>) {
        animationDataList = buildList {
            views.forEach { view ->
                val initialRotation = view.rotation

                add(RotateByData(initialRotation))
            }
        }
    }

    override fun updateAnimationByProportion(
        view: View,
        proportion: Float,
        animationData: RotateByData
    ) {
        view.rotation = animationData.initialRotation + proportion * angle
    }

    override fun clone(): Any {
        return RotateByAnimation(angle).also { clone ->
            cloneFromTo(this, clone)
        }
    }
}