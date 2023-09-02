package com.elihimas.orchestra.constrains.deeffectors

import android.view.View
import com.elihimas.orchestra.constrains.AffectedViews
import com.elihimas.orchestra.constrains.DeEffector
import com.elihimas.orchestra.constrains.Constrain

object TranslationDeEffector : DeEffector {

    private var initialTranslationX: Float? = null

    override fun applyEffect(source: View, affectedViews: AffectedViews) {
        val targetViews = affectedViews.views

        // TODO: move to before animation
        if (initialTranslationX == null)
            initialTranslationX = targetViews.first().get()?.translationX

        // TODO: refactor this
        if (affectedViews.constrain == Constrain.FollowVertically) {
            targetViews.forEach { targetView ->
                targetView.get()?.translationY = source.translationY
            }
        } else if (affectedViews.constrain == Constrain.FollowHorizontally) {
            targetViews.forEach { targetView ->
                targetView.get()?.translationX = source.translationX + (initialTranslationX ?: 0f)
            }
        } else if (affectedViews.constrain == Constrain.Follow) {
            targetViews.forEach { targetView ->
                targetView.get()?.translationX = source.translationX
                targetView.get()?.translationY = source.translationY
            }
        }
    }
}