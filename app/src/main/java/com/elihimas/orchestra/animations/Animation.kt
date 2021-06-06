package com.elihimas.orchestra.animations

import android.view.View
import android.view.ViewPropertyAnimator
import android.view.animation.Interpolator
import android.view.animation.LinearInterpolator
import com.elihimas.orchestra.OrchestraConfiguration


abstract class Animation(var duration: Long = OrchestraConfiguration.General.duration,
        //TODO make spacing work (for instance for Form Example)
                         var spacing: Long = OrchestraConfiguration.General.spacing) : Cloneable {
    var isInfinite = false
    internal var start = 0f
        set(value) {
            deltaTime = end - value
            field = value
        }
    internal var end = 0f
        set(value) {
            deltaTime = value - start
            field = value
        }
    var delay = 0L
    var interpolator: Interpolator = LinearInterpolator()

    var deltaTime = 0f

    //TODO: verify if this is being used
    open fun beforeAnimation(view: View) {}

    open fun runAnimation(view: View, endAction: Runnable?) {
        beforeAnimation(view)

        addAnimation(view, view.animate().also {
            it.withEndAction(endAction)
                    .setDuration(duration)
                    .setListener(null)
        })
    }

    open fun addAnimation(view: View, animation: ViewPropertyAnimator) {}

    public abstract override fun clone(): Any

    internal fun cloneFromTo(from: Animation, to: Animation) {
        to.duration = from.duration
        to.spacing = from.spacing
    }

    open fun updateAnimationByTime(view: View, time: Float) {
        val proportion = 1 - (end - time) / deltaTime

        if (proportion in 0.0..1.0) {
            val interpolatedProportion = interpolator.getInterpolation(proportion)
            updateAnimationByProportion(view, interpolatedProportion)
        }
    }

    open fun updateAnimationByProportion(view: View, proportion: Float) {
        TODO("Not yet implemented")
    }

    //TODO: review all init implementations to add support
    // to animate on multiple views
    // if an animation needs an view initialization the animation need to store each view data
    open fun init(vararg views: View) {}

    open fun calculateDuration(): Long = duration + delay

    open fun updateAnimationTimeBounds(baseTime: Float) {
        start = baseTime + delay
        end = start + duration
    }

    open fun finishAnimation(view: View) {
        updateAnimationByProportion(view, 1f)
    }
}