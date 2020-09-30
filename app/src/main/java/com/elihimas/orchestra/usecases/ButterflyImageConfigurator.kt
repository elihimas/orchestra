package com.elihimas.orchestra.usecases

import android.widget.ImageView
import com.elihimas.orchestra.Orchestra
import com.elihimas.orchestra.model.AnimateImageAnimations

class ButterflyImageConfigurator(private val butterflyImage: ImageView) {

    fun execute(animation: AnimateImageAnimations) =
            Orchestra.setup {
                val setupReference = on(butterflyImage)

                val doExecute = when (animation) {
                    AnimateImageAnimations.SlideOut,
                    AnimateImageAnimations.Translate, AnimateImageAnimations.Scale, AnimateImageAnimations.Rotate,
                    AnimateImageAnimations.CircularReveal, AnimateImageAnimations.Slide -> {
                        { setupReference.invisible() }
                    }
                    AnimateImageAnimations.FadeIn -> {
                        { setupReference.alpha(0f) }
                    }
                    AnimateImageAnimations.FadeOut -> {
                        { setupReference.alpha(1f) }
                    }
                }

                doExecute.invoke()
            }
}
