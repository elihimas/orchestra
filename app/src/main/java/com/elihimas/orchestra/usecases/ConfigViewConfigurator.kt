package com.elihimas.orchestra.usecases

import com.elihimas.orchestra.ConfigOrchestraView
import com.elihimas.orchestra.model.Examples
import java.lang.IllegalArgumentException

class ConfigViewConfigurator(private val configView: ConfigOrchestraView) {

    fun execute(example: Examples) {
        when (example) {
            Examples.FadeIn -> {
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
            Examples.Slide, Examples.SlideOut -> {
                configView.showDirectionControls = true
            }
            Examples.CircularReveal -> {

            }
            Examples.CoordinatorLayout -> {
                configView
            }
            Examples.Form -> {
                configView
            }
            else -> throw IllegalArgumentException("not implemented fot $example")
        }
    }
}