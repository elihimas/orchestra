package com.elihimas.orchestra.usecases

import android.view.View
import com.elihimas.orchestra.Direction
import com.elihimas.orchestra.Orchestra
import com.elihimas.orchestra.model.Examples
import java.lang.IllegalArgumentException

interface AnimationConfiguration {
    var duration: Long
    var initialAlpha: Float
    var finalAlpha: Float
    var scale: Float
    var direction: Direction
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
            Examples.SlideOut -> runSlideOut()
            Examples.CircularReveal -> runCircularReveal()
            else -> throw IllegalArgumentException("not implemented for: $example")
        }
    }

    private fun runFadeIn() {
        Orchestra.launch {
            on(target)
                    .fadeIn {
                        initialAlpha = configuration.initialAlpha
                        finalAlpha = configuration.finalAlpha
                        duration = configuration.duration
                    }
        }.then {
            animationEnded()
        }
    }

    private fun runFadeOut() {
        Orchestra.launch {
            on(target)
                    .fadeIn {
                        duration = configuration.duration
                        initialAlpha = configuration.initialAlpha
                        finalAlpha = configuration.finalAlpha
                    }
        }.then {
            animationEnded()
        }
    }

    private fun runTranslate() {
        Orchestra.launch {
            on(target)
                    .translate(0f, 600f) {
                        duration = configuration.duration
                    }
        }.then {
            animationEnded()
        }
    }

    private fun runScale() {
        val scale = configuration.scale

        Orchestra.launch {
            on(target)
                    .scale(scale) {
                        duration = configuration.duration
                    }
        }.then {
            animationEnded()
        }
    }

    private fun runSlide() {
        val direction = configuration.direction

        Orchestra.launch {
            on(target)
                    .slide(direction){
                        duration = configuration.duration
                    }
        }.then {
            animationEnded()
        }
    }

    private fun runSlideOut() {
        val direction = configuration.direction

        Orchestra.launch {
            on(target)
                    .slideOut(direction){
                        duration = configuration.duration
                    }
        }.then {
            animationEnded()
        }
    }

    private fun runCircularReveal() {
        Orchestra.launch {
            on(target)
                    .circularReveal(){
                        duration = configuration.duration
                    }
        }.then {
            animationEnded()
        }
    }

}
