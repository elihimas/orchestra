package com.elihimas.orchestra.activities

import com.elihimas.orchestra.Orchestra
import com.elihimas.orchestra.model.Examples
import kotlinx.android.synthetic.main.activity_centered_butterfly.*

class TranslateExampleActivity : CenteredImageActivity() {

    override fun getExample() = Examples.Translate

    override fun runAnimation() {
        val duration = configView.duration.toLong()

        Orchestra.launch {
            on(butterflyImage)
                    .translate(0f, 600f)
                    .duration(duration)
        }.then {
            configView.enableAnimateButton()
        }
    }
}
