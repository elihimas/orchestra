package com.elihimas.orchestra.usecases

import com.elihimas.orchestra.ConfigOrchestraView
import com.elihimas.orchestra.model.Examples

class ConfigViewConfigurator(private val configView: ConfigOrchestraView) {

    fun execute(example: Examples) {
        when (example) {
            Examples.FadeIn -> {
                configView
            }
            Examples.FadeOut -> {
                configView.initialAlpha = 1f
                configView.finalAlpha = 0f
            }
            Examples.Translate -> {
                configView.alphaControls = false
            }
            Examples.Scale -> {
                configView
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