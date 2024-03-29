package com.elihimas.orchestra.constrains.deeffectors

import android.view.View
import com.elihimas.orchestra.animations.Direction
import com.elihimas.orchestra.constrains.AffectedViews
import com.elihimas.orchestra.constrains.Constrain
import com.elihimas.orchestra.constrains.DeEffector

// TODO: refactor this
class DirectionalScaleSetupDeEffector(val direction: Direction) : DeEffector {
    override fun applyEffect(source: View, affectedViews: AffectedViews) {
        val targetViews = affectedViews.views

        if (affectedViews.constrain == Constrain.FollowHorizontally) {
            targetViews.forEach { targetView ->
                source.post {
                    targetView.get()?.translationX = source.translationX * 2
                }
            }
        } else if (affectedViews.constrain == Constrain.FollowVertically) {
            targetViews.forEach { targetView ->
                source.post {
                    targetView.get()?.translationY = source.translationY * 2
                }
            }
        }
    }

}
