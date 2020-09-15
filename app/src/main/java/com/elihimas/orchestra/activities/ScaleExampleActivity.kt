package com.elihimas.orchestra.activities

import com.elihimas.orchestra.Orchestra
import com.elihimas.orchestra.model.Examples
import kotlinx.android.synthetic.main.activity_centered_butterfly.*

class ScaleExampleActivity : CenteredImageActivity() {

    override fun getExample() = Examples.Scale

    override fun runAnimation() {
        val duration = configView.duration
        val scale = configView.scale

        Orchestra.launch {
            on(butterflyImage)
                    .scale(scale)
                    .duration(duration)
        }.then {
            configView.enableAnimateButton()
        }
    }
}
