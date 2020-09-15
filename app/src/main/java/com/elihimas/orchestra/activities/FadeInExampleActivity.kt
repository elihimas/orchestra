package com.elihimas.orchestra.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.elihimas.orchestra.Orchestra
import com.elihimas.orchestra.R
import kotlinx.android.synthetic.main.activity_centered_butterfly.*

class FadeInExampleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_centered_butterfly)

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
