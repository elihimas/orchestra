package com.elihimas.orchestra.animations.opacity

import android.view.View
import com.elihimas.orchestra.animations.StatefulAnimation

class OpacityToAnimation(private var targetAlpha: Float = 0f) :
    StatefulAnimation<OpacityData>() {

    override fun beforeAnimation(views: List<View>) {
        animationDataList = buildList {
            views.forEach { view ->
                val delta = targetAlpha - view.alpha
                add(OpacityData(view.alpha, delta))
            }
        }
    }

    override fun updateAnimationByProportion(
        view: View,
        proportion: Float,
        animationData: OpacityData
    ) {
        view.alpha = animationData.opacity + animationData.delta * proportion
    }

    override fun clone() = OpacityToAnimation(targetAlpha)
}