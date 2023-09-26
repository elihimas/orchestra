package com.elihimas.orchestra.animations

import android.view.View
import java.lang.IllegalStateException

abstract class StatefulAnimation<AnimationData> : Animation() {

    internal var animationDataList: List<AnimationData> = listOf()

    override fun updateAnimationByTime(views: List<View>, time: Float) {
        views.forEachIndexed { index, view ->
            val spacingDelay = index * timeSpacing
            val viewTime = time - spacingDelay - start
            val animationData = animationDataList[index]

            updateAnimationByTime(view, viewTime, animationData)

            view.updateAffectedViewsIfNecessary(this)
        }
    }

    private fun updateAnimationByTime(view: View, time: Float, animationData: AnimationData) {
        val proportion = time / duration

        if (proportion in 0.0..1.0) {
            val interpolatedProportion = interpolator.getInterpolation(proportion)
            updateAnimationByProportion(
                view,
                interpolatedProportion,
                animationData
            )
        }
    }

    abstract fun updateAnimationByProportion(
        view: View,
        proportion: Float,
        animationData: AnimationData
    )

    override fun updateAnimationByProportion(view: View, proportion: Float) {
        throw IllegalStateException("stateful animations should not call this method")
    }

    override fun finishAnimation(views: List<View>) {
        views.forEachIndexed { index, view ->
            val animationData = animationDataList[index]
            updateAnimationByProportion(view, 1f, animationData)
        }
        afterAnimation(views)
    }
}