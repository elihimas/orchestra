package com.elihimas.orchestra.usecases

import com.elihimas.orchestra.ConfigOrchestraView
import com.elihimas.orchestra.model.Examples

class ConfigViewConfigurator(private val configView: ConfigOrchestraView) {

    fun execute(example: Examples) {
        val doExecute = when (example) {
            Examples.FadeIn -> {
                { configView.showAlphaControls = true }
            }
            Examples.FadeOut -> {
                {
                    configView.showAlphaControls = true
                    configView.initialAlpha = 1f
                    configView.finalAlpha = 0f
                }
            }
            Examples.Translate, Examples.CircularReveal, Examples.BackgroundAndTextColor,
            Examples.Bouncing, Examples.Form, Examples.ConstrainsLayout,
            Examples.Rotate, Examples.CoordinatorLayout -> {
                {/*nothing to do*/ }
            }
            Examples.Scale -> {
                { configView.showScaleControls = true }
            }
            Examples.Slide, Examples.SlideOut -> {
                { configView.showDirectionControls = true }
            }
        }

        doExecute.invoke()
    }
}