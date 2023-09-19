package com.elihimas.orchestra.animations.translations

import android.view.View
import com.elihimas.orchestra.animations.StatefulAnimation
import com.elihimas.orchestra.constrains.DeEffector
import com.elihimas.orchestra.constrains.deeffectors.TranslationDeEffector
import com.elihimas.orchestra.references.TranslationReference

class TranslateToReferenceAnimation(private val reference: TranslationReference) :
    StatefulAnimation<TranslateToPositionAnimation>() {

    override fun beforeAnimation(views: List<View>) {
        animationDataList = buildList {
            views.forEach { view ->
                val point = reference.getPointFor(view)

                val destinationX = point.destinationX ?: view.x
                val destinationY = point.destinationY ?: view.y

                val translateDelegate = TranslateToPositionAnimation(destinationX, destinationY)
                translateDelegate.beforeAnimation(listOf(view))

                add(translateDelegate)
            }
        }
    }

    override fun updateAnimationByProportion(
        view: View,
        proportion: Float,
        animationData: TranslateToPositionAnimation
    ) {
        animationData.updateAnimationByProportion(
            view,
            proportion,
            animationData.animationDataList.first()
        )
    }

    override fun getDeEffector(): DeEffector {
        return TranslationDeEffector
    }

    override fun clone(): Any {
        return TranslateToReferenceAnimation(reference).also { clone ->
            cloneFromTo(this, clone)
        }
    }
}