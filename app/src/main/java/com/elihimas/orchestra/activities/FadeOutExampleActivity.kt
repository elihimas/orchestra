package com.elihimas.orchestra.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.elihimas.orchestra.Orchestra
import com.elihimas.orchestra.R
import kotlinx.android.synthetic.main.activity_centered_butterfly.*

class FadeOutExampleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_centered_butterfly)

        init()
    }

    private fun init() {
        configView.initialAlpha = 1f
        configView.finalAlpha = 0f

        Orchestra.setup {
            on(butterflyImage).alpha(1)
        }

        configView.onAnimate {
            runAnimation()
        }
    }

    private fun runAnimation() {
        val duration = configView.duration.toLong()
        val initialAlpha = configView.initialAlpha.toFloat()
        val finalAlpha = configView.finalAlpha.toFloat()

        Orchestra.launch {
            on(butterflyImage)
                    .fadeIn(initialAlpha, finalAlpha)
                    .duration(duration)
        }.then {
            configView.enableAnimateButton()
        }
    }
}
