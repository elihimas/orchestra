package com.elihimas.orchestra.blocks

import android.view.View
import com.elihimas.orchestra.animations.Animation

class ForeverAnimation(val animations: ViewReference) : Animation() {

    init {
        isInfinite = true
    }

    override fun updateAnimationTimeBounds(baseTime: Float) {
        var previousAnimationStart = baseTime
        animations.animations.forEach { animation ->
            animation.updateAnimationTimeBounds(baseTime = previousAnimationStart)

            previousAnimationStart = animation.end
        }

        start = baseTime
        end = previousAnimationStart
        duration = (end - start).toLong()//TODO verifi if can be deleted
    }

    override fun finishAnimation(view: View) {
        //nothing to do
    }

    override fun calculateDuration(): Long {
        return animations.calculateDuration()
    }

    override fun init(vararg views: View) {
        animations.animations.forEach { animation ->
            animation.init(*views)
        }
    }

    override fun updateAnimationByTime(view: View, time: Float) {
        animations.updateAnimations(time)
    }

    //TODO review if needs to clone constructor parameters
    override fun clone(): Any {
        return ForeverAnimation(animations)
    }
}