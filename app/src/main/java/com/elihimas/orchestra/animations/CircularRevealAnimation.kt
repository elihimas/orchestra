package com.elihimas.orchestra.animations

import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewPropertyAnimator
import androidx.core.animation.doOnEnd
import kotlin.math.hypot

//TODO for now this class creates it's own ticker
class CircularRevealAnimation : Animation() {

    var animationStarted = false
    override fun updateAnimationByProportion(view: View, proportion: Float) {
        //TODO not working for multiple views
        if (!animationStarted) {
            view.post {
                animationStarted = true
                runAnimation(view, null)
            }
        }
    }

    override fun clone(): Any {
        return CircularRevealAnimation().also {
            cloneFromTo(it, this)
        }
    }

    override fun runAnimation(view: View, endAction: Runnable?) {
        val cx = view.width / 2
        val cy = view.height / 2

        // get the final radius for the clipping circle
        val finalRadius = hypot(cx.toDouble(), cy.toDouble()).toFloat()

        // create the animator for this view (the start radius is zero)
        val anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, 0f, finalRadius)
        // make the view visible and start the animation
        view.visibility = View.VISIBLE
        anim.duration = duration

        anim.doOnEnd { endAction?.run() }

        anim.start()
    }

    override fun addAnimation(view: View, animation: ViewPropertyAnimator) {

    }
}