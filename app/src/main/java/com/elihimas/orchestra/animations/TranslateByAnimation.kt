package com.elihimas.orchestra.animations

import android.view.View
import com.elihimas.orchestra.constrains.deeffectors.TranslationDeEffector

class TranslateByAnimation(private val x: Float, private val y: Float) : Animation() {

    private var initialX = 0f
    private var initialY = 0f

    override fun beforeAnimation(vararg views: View) {
        initialX = views[0].translationX
        initialY = views[0].translationY
    }

    override fun updateAnimationByProportion(view: View, proportion: Float) {
        val newX = initialX + proportion * x
        val newY = initialY + proportion * y

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