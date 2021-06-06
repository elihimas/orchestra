package com.elihimas.orchestra.actions

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.view.View
import android.view.animation.BaseInterpolator
import android.view.animation.BounceInterpolator
import android.view.animation.Interpolator
import androidx.annotation.DimenRes
import com.elihimas.orchestra.OrchestraConfiguration

data class WaitAnimateDuration(val animationDuration: Long = OrchestraConfiguration.AnimateAndWaitDuration.animationDuration,
                               val waitDuration: Long = OrchestraConfiguration.AnimateAndWaitDuration.waitDuration)

class BounceAction(var duration: WaitAnimateDuration = WaitAnimateDuration(),
                   var startDelay: Long = OrchestraConfiguration.Bounce.startDelay,
                   @DimenRes var heightResId: Int = OrchestraConfiguration.Bounce.height,
                   var bounceHeight: Float? = null) : SetupAction() {

    override fun runSetup(view: View) {
        val bounceHeight = this.bounceHeight ?: view.context.resources.getDimension(heightResId)
        val animator = ObjectAnimator.ofFloat(view, "translationY", 0f, bounceHeight, 0f)
        animator.duration = duration.animationDuration + duration.waitDuration

        animator.interpolator = AnimateWaitInterpolator(duration, BounceInterpolator())
        animator.repeatCount = ValueAnimator.INFINITE
        animator.startDelay = startDelay
        animator.start()
    }

    class AnimateWaitInterpolator(duration: WaitAnimateDuration, private val interpolator: Interpolator) : BaseInterpolator() {

        private val animationDuration: Float
        private val multiplier: Float

        init {
            val totalDuration = duration.animationDuration + duration.waitDuration

            animationDuration = duration.animationDuration.toFloat() / totalDuration
            multiplier = 1 / animationDuration
        }

        override fun getInterpolation(input: Float): Float {
            if (input >= animationDuration) {
                return 0f
            }

            val interpolatorInput = input * multiplier
            return interpolator.getInterpolation(interpolatorInput)
        }

    }
}