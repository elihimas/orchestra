package com.elihimas.orchestra.constrains.deeffectors

import android.view.View
import com.elihimas.orchestra.constrains.AffectedViews
import com.elihimas.orchestra.constrains.DeEffector
import com.elihimas.orchestra.constrains.Constrain

class DirectionalScaleAnimationDeEffector : DeEffector {

    override fun applyEffect(source: View, affectedViews: AffectedViews) {
        val targetViews = affectedViews.views

        // TODO: refactor this
        if (affectedViews.constrain == Constrain.TopToBottomOf) {
            targetViews.forEach { targetView ->
                targetView.get()?.translationY = source.translationY * 2
            }
        } else if (affectedViews.constrain == Constrain.BottomToTopOf) {
            targetViews.forEach { targetView ->
                targetView.get()?.translationY = source.translationY * 2
            }
        } else if (affectedViews.constrain == Constrain.RightToLeftOf) {
            targetViews.forEach { targetView ->
                targetView.get()?.translationX = source.translationX * 2
            }
        } else if (affectedViews.constrain == Constrain.LeftToRightOf) {
            targetViews.forEach { targetView ->
                targetView.get()?.translationX = source.translationX * 2
            }
        }
    }
}
