package com.elihimas.orchestra

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewGroup
import android.view.ViewPropertyAnimator
import android.view.animation.TranslateAnimation
import androidx.core.animation.doOnEnd
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


abstract class Action(var duration: Long = 2600L, var spacing: Long = 0) {
    open fun runAnimation(view: View, endAction: Runnable?) {
        beforeAnimation(view)
        addAnimation(view, view.animate().also {
            it.withEndAction(endAction)
                    .setDuration(duration)
                    .setListener(null)
        })
    }

    open fun beforeAnimation(view: View) {}

    abstract fun addAnimation(view: View, animation: ViewPropertyAnimator)
}

class CircularRevealAction : Action() {
    override fun runAnimation(view: View, endAction: Runnable?) {
        val cx = view.width / 2
        val cy = view.height / 2

        // get the final radius for the clipping circle
        val finalRadius = Math.hypot(cx.toDouble(), cy.toDouble()).toFloat()

        // create the animator for this view (the start radius is zero)
        val anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, 0f, finalRadius)
        // make the view visible and start the animation
        view.visibility = View.VISIBLE
        anim.addListener(object : AnimatorListenerAdapter() {

            override fun onAnimationEnd(animation: Animator?) {
                endAction?.run()
            }
        })
        anim.start()
    }

    override fun addAnimation(view: View, animation: ViewPropertyAnimator) {

    }
}

class SlideAction : Action() {
    override fun runAnimation(view: View, endAction: Runnable?) {
        val translate = TranslateAnimation(
                0f,  // fromXDelta
                0f,  // toXDelta
                view.height.toFloat(),  // fromYDelta
                0f) // toYDelta
        translate.duration = duration
        translate.fillAfter = true

        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED))

        val finalHeight = view.measuredHeight
        val initialTopMargin = (view.layoutParams as ViewGroup.MarginLayoutParams).topMargin

        val animator = ValueAnimator.ofInt(0, finalHeight).apply {
            addUpdateListener { valueAnimator ->
                val value = valueAnimator.animatedValue as Int
                view.layoutParams = (view.layoutParams as ViewGroup.MarginLayoutParams).apply {
                    height = value
                    topMargin = initialTopMargin - value
                }
            }
            this.duration = this@SlideAction.duration
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    endAction?.run()
                }
            })
        }

        animator.start()
//
//        translate.setAnimationListener(object : Animation.AnimationListener {
//            override fun onAnimationRepeat(animation: Animation?) {
//            }
//
//            override fun onAnimationEnd(animation: Animation?) {
//                endAction?.run()
//            }
//
//            override fun onAnimationStart(animation: Animation?) {
//            }
//
//        })

        //view.startAnimation(translate)
    }

    override fun addAnimation(view: View, animation: ViewPropertyAnimator) {
        //nothing to do
    }

}

class ParallelActions(private val reference: Animations) : Action() {

    override fun addAnimation(view: View, animation: ViewPropertyAnimator) {
        reference.actions.forEach { action ->
            action.addAnimation(view, animation)
        }
    }
}

class DelayAction(duration: Long) : Action(duration) {
    override fun runAnimation(view: View, endAction: Runnable?) {
        GlobalScope.launch {
            delay(duration)
            endAction?.run()
        }
    }

    override fun addAnimation(view: View, animation: ViewPropertyAnimator) {
        //nothing to do
    }
}

class TranslateAction(private val x: Float, private val y: Float) : Action() {

    override fun addAnimation(view: View, animation: ViewPropertyAnimator) {
        animation
                .translationX(x)
                .translationY(y)
    }
}

class CropShowAction : Action() {

    override fun addAnimation(view: View, animation: ViewPropertyAnimator) {
        animation.setUpdateListener { animator ->
            view.layoutParams.let {
                it.width = (animator.animatedFraction * 100).toInt()
                view.layoutParams = it
            }
        }
    }
}


class ScaleAction(private val scale: Float) : Action() {
    override fun beforeAnimation(view: View) {
        view.scaleX
    }

    override fun addAnimation(view: View, animation: ViewPropertyAnimator) {
        animation
                .scaleX(scale)
                .scaleY(scale)
    }

}