package com.elihimas.orchestra.animations.colors

import android.animation.ArgbEvaluator
import android.view.View
import androidx.annotation.ColorRes
import com.elihimas.orchestra.animations.Animation

abstract class ColorAnimation(@ColorRes private val initialColorRes: Int, @ColorRes private val finalColorRes: Int) : Animation() {

    private val evaluator = ArgbEvaluator()
    protected var initialColor: Int = 0
    protected var finalColor: Int = 0

    override fun beforeAnimation(vararg views: View) {
        val context = views[0].context

        initialColor = context.getColor(initialColorRes)
        finalColor = context.getColor(finalColorRes)
    }

    override fun updateAnimationByProportion(view: View, proportion: Float) {
        val color = evaluator.evaluate(proportion, initialColor, finalColor)
        update(view, color as Int)
    }

    abstract fun update(view: View, color: Int)
}