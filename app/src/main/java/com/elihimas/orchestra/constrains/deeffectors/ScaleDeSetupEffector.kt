package com.elihimas.orchestra.constrains.deeffectors

import android.view.View
import com.elihimas.orchestra.constrains.AffectedViews
import com.elihimas.orchestra.constrains.Constrain
import com.elihimas.orchestra.constrains.DeEffector

class ScaleDeSetupEffector(val scaleX: Float, val scaleY: Float) : DeEffector {

    override fun applyEffect(source: View, affectedViews: AffectedViews) {
        val targetViews = affectedViews.views

        // TODO: implement and test other constrains
        if (affectedViews.constrain == Constrain.TopToBottomOf) {
            targetViews.forEach { targetView ->
                source.post {
                    targetView.get()?.translationY = source.height.toFloat() * -1
                }
            }
        } else if (affectedViews.constrain == Constrain.BottomToTopOf) {
            // TODO: test this
            targetViews.forEach { targetView ->
                source.post {
                    targetView.get()?.translationY = source.height * (1 - scaleY) / 2
                }
            }
        }
    }
}