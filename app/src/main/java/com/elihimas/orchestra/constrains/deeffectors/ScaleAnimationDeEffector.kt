package com.elihimas.orchestra.constrains.deeffectors

import android.view.View
import com.elihimas.orchestra.constrains.AffectedViews
import com.elihimas.orchestra.constrains.Constrain
import com.elihimas.orchestra.constrains.DeEffector

class ScaleAnimationDeEffector : DeEffector {
    override fun applyEffect(source: View, affectedViews: AffectedViews) {
        val targetViews = affectedViews.views

        // TODO: implement and test other constrains
        if (affectedViews.constrain == Constrain.BottomToTopOf
            || affectedViews.constrain == Constrain.FollowVertically
        ) {
            targetViews.forEach { targetView ->
                source.post {
                    targetView.get()?.translationY = source.height * (1 - source.scaleY) / 2
                }
            }
        }
    }
}