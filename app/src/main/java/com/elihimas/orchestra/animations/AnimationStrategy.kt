package com.elihimas.orchestra.animations

import android.view.View

interface AnimationStrategy<AnimationData> {
    fun createAnimationDataFor(views: List<View>): List<AnimationData>
    fun update(view: View, proportion: Float, animationData: AnimationData)
}