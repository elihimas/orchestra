package com.elihimas.orchestra.animations

import android.view.View

interface AnimationStrategy {
    fun init(views: List<View>)
    fun update(view: View, proportion: Float)
}