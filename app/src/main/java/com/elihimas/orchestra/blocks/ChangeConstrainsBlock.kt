package com.elihimas.orchestra.blocks

import android.app.Activity
import android.transition.ChangeBounds
import android.transition.Transition
import android.transition.TransitionManager
import android.view.animation.AnticipateOvershootInterpolator
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.elihimas.orchestra.Orchestra
import com.elihimas.orchestra.coroutineDelay

//TODO review this class
class ChangeConstrainsBlock(private val root: ConstraintLayout, private val layoutId: Int, var duration: Long = 2600)
    : Block() {

    override fun calculateDuration() = duration

    override suspend fun runBlock(orchestra: Orchestra) {
        val transition: Transition = ChangeBounds()
        transition.interpolator = AnticipateOvershootInterpolator(0f)
        transition.duration = duration

        TransitionManager.beginDelayedTransition(root, transition)
        val constraintSet = ConstraintSet()
        constraintSet.clone(root.context, layoutId)

        (root.context as Activity).runOnUiThread {
            constraintSet.applyTo(root)
        }

        coroutineDelay(transition.duration)
    }
}
