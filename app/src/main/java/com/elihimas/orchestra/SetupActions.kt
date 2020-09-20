package com.elihimas.orchestra

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.view.View
import android.view.animation.BaseInterpolator
import android.view.animation.BounceInterpolator
import android.view.animation.Interpolator
import androidx.annotation.DimenRes
import androidx.annotation.IdRes


abstract class SetupAction {
    fun runSetup(views: Array<out View>) {
        views.forEach {
            runSetup(it)
        }
    }

    abstract fun runSetup(view: View)
}

class AlphaAction(private val value: Float) : SetupAction() {
    override fun runSetup(view: View) {
        view.alpha = value
    }
}

class SetupScaleAction(private val value: Float) : SetupAction() {
    override fun runSetup(view: View) {
        view.apply {
            scaleX = value
            scaleY = value
        }
    }
}

class InvisibleAction() : SetupAction() {
    override fun runSetup(view: View) {
        view.visibility = View.INVISIBLE
    }

}

data class WaitAnimateDuration(val animationDuration: Long = OrchestraConfiguration.AnimateAndWaitDuration.animationDuration,
                               val waitDuration: Long = OrchestraConfiguration.AnimateAndWaitDuration.waitDuration)

class BounceAction(var duration: WaitAnimateDuration = WaitAnimateDuration(),
                   var startDelay: Long = OrchestraConfiguration.Bounce.startDelay,
                   @DimenRes var heightResId: Int = OrchestraConfiguration.Bounce.height,
                   var bounceHeight: Float? = null) : SetupAction() {

    override fun runSetup(view: View) {
        val bounceHeight = this.bounceHeight ?: view.context.resources.getDimension(heightResId);
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

class OnClick(private val onClickListener: View.OnClickListener) : SetupAction() {
    override fun runSetup(view: View) {
        view.setOnClickListener(onClickListener)
    }

}