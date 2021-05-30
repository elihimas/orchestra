package com.elihimas.orchestra.animations

import android.view.View

interface AnimationStrategy {
    fun init(vararg views: View)
    fun update(view: View, proportion: Float)
}