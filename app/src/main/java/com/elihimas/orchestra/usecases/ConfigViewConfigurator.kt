package com.elihimas.orchestra.usecases

import com.elihimas.orchestra.ConfigOrchestraView
import com.elihimas.orchestra.model.Examples

class ConfigViewConfigurator(private val configView: ConfigOrchestraView) {

    fun execute(example: Examples) {
        when (example) {
            Examples.FadeIn -> {
                configView
                configView.showAlphaControls = true
            }
            Examples.FadeOut -> {
                configView.showAlphaControls = true
                configView.initialAlpha = 1f
                configView.finalAlpha = 0f
            }
            Examples.Translate -> {
            }
            Examples.Scale -> {
                configView.showScaleControls = true
            }
            Examples.CoordinatorLayout -> {
                configView
            }
            Examples.Form -> {
                configView
            }
        }
    }
}