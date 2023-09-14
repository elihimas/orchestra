package com.elihimas.orchestra.animations.translations

import android.view.View
import com.elihimas.orchestra.animations.Animation
import com.elihimas.orchestra.constrains.deeffectors.TranslationDeEffector

class ResetTranslationAnimation() : Animation() {

    private var translationXDelta: Float = 0f
    private var translationYDelta: Float = 0f
    override fun beforeAnimation(vararg views: View) {
        translationXDelta = views[0].translationX
        translationYDelta = views[0].translationY
    }

    override fun updateAnimationByProportion(view: View, proportion: Float) {
        val reversedProportion = 1 - proportion

        view.translationX = translationXDelta * reversedProportion
        view.translationY = translationYDelta * reversedProportion
    }

    override fun getDeEffector() = TranslationDeEffector

    override fun clone(): ResetTranslationAnimation {
        return ResetTranslationAnimation()
    }
}