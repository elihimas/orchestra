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

    override fun updateAnimationByTime(views: List<View>, time: Float) {
        reference.animations.forEach { animation ->
            animation.updateAnimationByTime(views, time)
        }

        views.forEach { view ->
            view.updateAffectedViewsIfNecessary(this)
        }
    }

    override fun finishAnimation(views: List<View>) {
        reference.animations.forEach { animation ->
            if (animation is StatefulAnimation<*>) {
                finishStatefulAnimation(animation, views)
            } else {
                views.forEach { view ->
                    animation.updateAnimationByProportion(view, 1f)
                }
            }
        }

        afterAnimation(views)
    }

    private fun <T> finishStatefulAnimation(animation: StatefulAnimation<T>, views: List<View>) {
        views.forEachIndexed { index, view ->
            val animationData = animation.animationDataList[index]

            animation.updateAnimationByProportion(
                view, 1f, animationData
            )
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