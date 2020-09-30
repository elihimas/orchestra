package com.elihimas.orchestra.usecases

import com.elihimas.orchestra.model.AnimateImageAnimations
import com.elihimas.orchestra.ui.views.ConfigOrchestraView

//TODO verify if this class will be necessary
class ConfigViewConfigurator(private val configView: ConfigOrchestraView) {

    fun execute(example: AnimateImageAnimations) {
        val doExecute = when (example) {
            AnimateImageAnimations.FadeIn -> {
                { configView.showAlphaControls = true }
            }
            AnimateImageAnimations.FadeOut -> {
                {
                    configView.showAlphaControls = true
                    configView.initialAlpha = 1f
                    configView.finalAlpha = 0f
                }
            }
            AnimateImageAnimations.Translate, AnimateImageAnimations.CircularReveal, AnimateImageAnimations.Rotate,
            AnimateImageAnimations.Scale -> {
                { configView.showScaleControls = true }
            }
            AnimateImageAnimations.Slide, AnimateImageAnimations.SlideOut -> {
                { configView.showDirectionControls = true }
            }
        }

        doExecute.invoke()
    }
}