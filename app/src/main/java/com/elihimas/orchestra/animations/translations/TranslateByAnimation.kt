package com.elihimas.orchestra.animations.translations

import android.view.View
import com.elihimas.orchestra.animations.StatefulAnimation
import com.elihimas.orchestra.constrains.deeffectors.TranslationDeEffector

class TranslateByAnimation(private val x: Float, private val y: Float) :
    StatefulAnimation<TranslateByData>() {

    override fun beforeAnimation(views: List<View>) {
        animationDataList = buildList {
            views.forEach { view ->
                val initialX = view.translationX
                val initialY = view.translationY

                add(TranslateByData(initialX, initialY))
            }
        }
    }

    override fun updateAnimationByProportion(
        view: View,
        proportion: Float,
        animationData: TranslateByData
    ) {
        val newX = animationData.initialX + proportion * x
        val newY = animationData.initialY + proportion * y

        view.translationX = newX
        view.translationY = newY
    }

    override fun getDeEffector() = TranslationDeEffector

    override fun clone(): Any {
        return TranslateByAnimation(x, y).also { clone ->
            cloneFromTo(this, clone)
        }
    }
}