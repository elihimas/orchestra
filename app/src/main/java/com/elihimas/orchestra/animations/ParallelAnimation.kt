package com.elihimas.orchestra.animations

import android.view.View
import android.view.ViewPropertyAnimator
import com.elihimas.orchestra.Animations

class ParallelAnimation(private val reference: Animations) : Animation() {

    override fun init(vararg views: View) {
        reference.animations.forEach { animation ->
            animation.init(*views)
        }
    }

    override fun updateAnimationByTime(view: View, time: Float) {
        reference.animations.forEach { animation ->
            animation.updateAnimationByTime(view, time)
        }
    }

    override fun updateAnimationByProportion(view: View, proportion: Float) {
        reference.animations.forEach { animation ->
            animation.updateAnimationByProportion(view, proportion)
        }
    }

    override fun clone(): Any {
        return ParallelAnimation(reference.clone()).also { clone ->
            cloneFromTo(this, clone)
        }
    }

    override fun addAnimation(view: View, animation: ViewPropertyAnimator) {
    }

    override fun runAnimation(view: View, endAction: Runnable?) {
        reference.animations.forEach { action ->
            action.runAnimation(view, endAction)
        }
    }

    override fun calculateDuration(): Long {
        val maxDuration = reference.animations.maxOf { animation -> animation.calculateDuration() }
        this.duration = maxDuration
        return maxDuration
    }

    override fun updateAnimationTimeBounds(baseTime: Float) {
        reference.animations.forEach { animation ->
            animation.updateAnimationTimeBounds(baseTime)
        }

        val duration = reference.animations.maxOf { animation -> animation.calculateDuration() }

        start = baseTime
        end = baseTime + duration
    }
}