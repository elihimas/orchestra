package com.elihimas.orchestra.animations

import android.view.View
import android.view.ViewPropertyAnimator
import android.view.animation.Interpolator
import android.view.animation.LinearInterpolator
import com.elihimas.orchestra.OrchestraConfiguration
import com.elihimas.orchestra.constrains.DeEffector
import com.elihimas.orchestra.constrains.References

abstract class Animation(
    var duration: Long = OrchestraConfiguration.General.duration,
    // TODO: create a spacing strategy regarding the start of the next animation
    //  the end of the current animation should be one of:
    //      start + duration
    //      start + duration + spacing increment  (current default)
    var spacing: Long = OrchestraConfiguration.General.spacing
) : Cloneable {
    var isInfinite = false
    internal var start = 0f
    internal var end = 0f
    var delay = 0L
    var interpolator: Interpolator = LinearInterpolator()

    //TODO: review all init implementations to add support
    // to animate on multiple views
    // if an animation needs an view initialization the animation need to store each view data
    open fun beforeAnimation(views: List<View>) {}

    open fun afterAnimation(views: List<View>) {}

    open fun runAnimation(view: View, endAction: Runnable?) {
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

    open fun updateAnimationByTime(views: List<View>, time: Float) {
        views.forEachIndexed { index, view ->
            val spacingDelay = index * spacing
            val viewTime = time - spacingDelay - delay - start

            updateAnimationByTime(view, viewTime)

            view.updateAffectedViewsIfNecessary(this)
        }
    }

    open fun updateAnimationByTime(view: View, time: Float) {
        val proportion = time / duration

        if (proportion in 0.0..1.0) {
            val interpolatedProportion = interpolator.getInterpolation(proportion)
            updateAnimationByProportion(
                view,
                interpolatedProportion
            )
        }
    }

    open fun updateAnimationByProportion(view: View, proportion: Float) {
        TODO("Not yet implemented")
    }

    internal open fun calculateDuration(viewsCount: Int): Long =
        delay + duration + (viewsCount - 1) * spacing

    open fun updateAnimationTimeBounds(baseTime: Float, viewsCount: Int) {
        start = baseTime + delay
        end = baseTime + calculateDuration(viewsCount)
    }

    open fun finishAnimation(views: List<View>) {
        views.forEach {  view ->
            updateAnimationByProportion(view, 1f)
        }
        afterAnimation(views)
    }

    open fun getDeEffector(): DeEffector? = null

    // TODO: move this to a proper location
    fun View.updateAffectedViewsIfNecessary(animation: Animation) {
        val deEffector = animation.getDeEffector()
        val affectedViews = References.map[this]

        if (affectedViews?.views?.isNotEmpty() == true) {
            deEffector?.applyEffect(this, affectedViews)
        }
    }
}