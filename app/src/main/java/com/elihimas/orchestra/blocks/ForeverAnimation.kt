package com.elihimas.orchestra.blocks

import android.view.View
import com.elihimas.orchestra.animations.Animation

class ForeverAnimation(val animations: ViewReference) : Animation() {

    init {
        isInfinite = true
    }

    override fun updateAnimationTimeBounds(baseTime: Float, viewsCount: Int) {
        var previousAnimationStart = baseTime
        animations.animations.forEach { animation ->
            animation.updateAnimationTimeBounds(baseTime = previousAnimationStart, viewsCount)

            previousAnimationStart = animation.end
        }

        start = baseTime
        end = previousAnimationStart
        duration = (end - start).toLong()//TODO verify if can be deleted
    }

    override fun calculateDuration(viewsCount: Int): Long {
        return animations.calculateDuration()
    }

    override fun beforeAnimation(views: List<View>) {
        animations.animations.forEach { animation ->
            animation.beforeAnimation(views)
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
