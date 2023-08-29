package com.elihimas.orchestra.constrains.deeffectors

import android.view.View
import com.elihimas.orchestra.constrains.AffectedViews
import com.elihimas.orchestra.constrains.DeEffector
import com.elihimas.orchestra.constrains.Constrain

object DirectionalScaleAnimationDeEffector : DeEffector {

    private val verticalConstrains =
        listOf(Constrain.BottomToTopOf, Constrain.TopToBottomOf, Constrain.FollowVertically)

    private val horizontalConstrains =
        listOf(Constrain.LeftToRightOf, Constrain.RightToLeftOf, Constrain.FollowHorizontally)

    override fun applyEffect(source: View, affectedViews: AffectedViews) {
        val targetViews = affectedViews.views

        // TODO: refactor this
        if (affectedViews.constrain in verticalConstrains) {
            targetViews.forEach { targetView ->
                targetView.get()?.translationY = source.translationY * 2
            }
        } else if (affectedViews.constrain in horizontalConstrains) {
            targetViews.forEach { targetView ->
                targetView.get()?.translationX = source.translationX * 2
            }
        } else if (affectedViews.constrain == Constrain.Follow) {
            targetViews.forEach { targetView ->
                targetView.get()?.translationX = source.translationX * 2
                targetView.get()?.translationY = source.translationY * 2
            }
        }
    }
}
