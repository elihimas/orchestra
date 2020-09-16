package com.elihimas.orchestra.usecases

import android.view.View
import com.elihimas.orchestra.Orchestra
import com.elihimas.orchestra.model.Examples
import kotlin.math.E

interface AnimationConfiguration {
    var duration: Long
    var initialAlpha: Float
    var finalAlpha: Float
    var scale: Float
}

class AnimatorRunner(private val configuration: AnimationConfiguration,
                     private val target: View,
                     private val animationEnded: () -> Unit) {

    fun execute(example: Examples) {
        when (example) {
            Examples.FadeIn -> runFadeIn()
            Examples.FadeOut -> runFadeOut()
            Examples.Translate -> runTranslate()
            Examples.Scale -> runScale()
            Examples.Slide -> runSlide()
            Examples.CircularReveal -> runCircularReveal()
            Examples.CoordinatorLayout, Examples.Form -> TODO()
        }
    }

    private fun runFadeIn() {
        val duration = configuration.duration
        val initialAlpha = configuration.initialAlpha
        val finalAlpha = configuration.finalAlpha

        Orchestra.launch {
            on(target)
                    .fadeIn(initialAlpha, finalAlpha)
                    .duration(duration)
        }.then {
            animationEnded()
        }
    }

    private fun runFadeOut() {
        val duration = configuration.duration
        val initialAlpha = configuration.initialAlpha
        val finalAlpha = configuration.finalAlpha

        Orchestra.launch {
            on(target)
                    .fadeIn(initialAlpha, finalAlpha)
                    .duration(duration)
        }.then {
            animationEnded()
        }
    }

    private fun runTranslate() {
        val duration = configuration.duration

        Orchestra.launch {
            on(target)
                    .translate(0f, 600f)
                    .duration(duration)
        }.then {
            animationEnded()
        }
    }

    private fun runScale() {
        val duration = configuration.duration
        val scale = configuration.scale

        Orchestra.launch {
            on(target)
                    .scale(scale)
                    .duration(duration)
        }.then {
            animationEnded()
        }
    }

    private fun runSlide() {
        val duration = configuration.duration

        Orchestra.setup {
            on(target).slideHide()
        }

        Orchestra.launch {
            on(target)
                    .slide()
                    .duration(duration)
        }.then {
            animationEnded()
        }
    }

    private fun runCircularReveal() {
        val duration = configuration.duration

        Orchestra.launch {
            on(target)
                    .circularReveal()
                    .duration(duration)
        }.then {
            animationEnded()
        }
    }

}
