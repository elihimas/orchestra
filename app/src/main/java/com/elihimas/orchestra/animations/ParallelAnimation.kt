package com.elihimas.orchestra.animations

import android.view.View
import android.view.ViewPropertyAnimator
import com.elihimas.orchestra.blocks.AnimationsBlock

class ParallelAnimation(private val reference: AnimationsBlock) : Animation() {

    override fun beforeAnimation(views: List<View>) {
        reference.animations.forEach { animation ->
            animation.beforeAnimation(views)
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

    override fun calculateDuration(viewsCount: Int): Long {
        val maxDuration =
            reference.animations.maxOf { animation -> animation.calculateDuration(viewsCount) }
        this.duration = maxDuration
        return maxDuration
    }

    override fun updateAnimationTimeBounds(baseTime: Float, viewsCount: Int) {
        reference.animations.forEach { animation ->
            animation.updateAnimationTimeBounds(baseTime, viewsCount)
        }

        val duration =
            reference.animations.maxOf { animation -> animation.calculateDuration(viewsCount) }

        start = baseTime
        end = baseTime + duration
    }
}