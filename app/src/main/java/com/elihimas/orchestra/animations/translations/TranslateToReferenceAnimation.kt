package com.elihimas.orchestra.animations.translations

import android.view.View
import com.elihimas.orchestra.animations.Animation
import com.elihimas.orchestra.constrains.DeEffector
import com.elihimas.orchestra.references.TranslationReference

class TranslateToReferenceAnimation(private val reference: TranslationReference) :
    Animation() {

    private lateinit var translateDelegate: TranslateToPositionAnimation

    override fun beforeAnimation(vararg views: View) {
        val point = reference.getPointFor(views[0])

        val destinationX = point.destinationX ?: views[0].x
        val destinationY = point.destinationY ?: views[0].y

        translateDelegate = TranslateToPositionAnimation(destinationX, destinationY)
        translateDelegate.beforeAnimation(*views)
    }

    override fun updateAnimationByProportion(view: View, proportion: Float) {
        translateDelegate.updateAnimationByProportion(view, proportion)
    }

    override fun getDeEffector(): DeEffector {
        return translateDelegate.getDeEffector()
    }

    override fun clone(): Any {
        return TranslateToReferenceAnimation(reference).also { clone ->
            cloneFromTo(this, clone)
        }
    }
}