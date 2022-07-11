package com.elihimas.orchestra.blocks

import android.transition.ChangeBounds
import android.transition.Transition
import android.transition.TransitionManager
import android.util.Log
import android.view.animation.LinearInterpolator
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet

class ChangeConstrainsBlock(
    private val root: ConstraintLayout,
    private val layoutId: Int,
    var duration: Long = 1200
) : Block() {

    override fun resetForeverData() {
		// TODO: review this
    }

    override fun checkHasForeverAnimation(): Boolean = false

    override fun calculateDuration() = duration

    override fun runBlock() {
        Log.d("tag", "***")
        val transition: Transition = ChangeBounds()
        transition.interpolator = LinearInterpolator()
        transition.duration = duration

        TransitionManager.beginDelayedTransition(root, transition)
        val constraintSet = ConstraintSet()
        constraintSet.clone(root.context, layoutId)
        constraintSet.applyTo(root)
    }
}
