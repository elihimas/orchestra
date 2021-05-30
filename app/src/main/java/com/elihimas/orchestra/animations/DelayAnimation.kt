package com.elihimas.orchestra.animations

import android.view.View

class DelayAnimation(duration: Long) : Animation(duration) {
    override fun clone() = DelayAnimation(duration)

    override fun updateAnimationByProportion(view: View, proportion: Float) {
        //nothing to do
    }
}