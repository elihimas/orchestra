package com.elihimas.orchestra.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.elihimas.orchestra.Orchestra
import com.elihimas.orchestra.R
import kotlinx.android.synthetic.main.activity_fade_in_example.*

class FadeInExampleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fade_in_example)

        init()
    }

    private fun init() {
        Orchestra.setup {
            on(butterflyImage).alpha(0)
        }

        configView.onAnimate {
            runAnimation()
        }
    }

    private fun runAnimation() {
        val duration = configView.duration.toLong()
        val initialAlpha = configView.initialAlpha.toFloat()
        val finalAlpha = configView.finalAlpha.toFloat() / 100

        Orchestra.launch {
            on(butterflyImage)
                    .fadeIn(initialAlpha, finalAlpha)
                    .duration(duration)
        }.then {
            configView.enableAnimateButton()
        }
    }
}
