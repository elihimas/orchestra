package com.elihimas.orchestra.activities

import com.elihimas.orchestra.Orchestra
import com.elihimas.orchestra.model.Examples
import kotlinx.android.synthetic.main.activity_centered_butterfly.*

class FadeInExampleActivity : CenteredImageActivity() {

    override fun getExample() = Examples.FadeIn

    override fun runAnimation() {
        val duration = configView.duration
        val initialAlpha = configView.initialAlpha
        val finalAlpha = configView.finalAlpha

        Orchestra.launch {
            on(butterflyImage)
                    .fadeIn(initialAlpha, finalAlpha)
                    .duration(duration)
        }.then {
            configView.enableAnimateButton()
        }
    }
}
