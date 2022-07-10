package com.elihimas.orchestra.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.elihimas.orchestra.Orchestra
import com.elihimas.orchestra.R
import kotlinx.android.synthetic.main.activity_fade_example.*

class FadeExampleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fade_example)
        init()
    }

    private fun init() {
        btStart.setOnClickListener {
            runAnimation()
        }
        btStop.setOnClickListener {
            stopAnimation()
        }
    }

    private fun runAnimation() {
        val smallScale = 0.3f
        val fullScale = 1

        Orchestra.launch {
            val duration = 600L
            val blinkAnimation = createAnimation()
                .parallel {
                    fadeOut {
                        this.duration = duration
                    }
                    scale(smallScale) {
                        this.duration = duration
                    }
                }
                .delay(600)
                .parallel {
                    fadeIn {
                        this.duration = duration
                    }
                    scale(fullScale) {
                        this.duration = duration
                    }
                }


            parallel {
                on(squareLeft).repeat(3, blinkAnimation)
                on(squareCenter).delay(400).repeat(3, blinkAnimation)
                on(squareRight).delay(800).repeat(3, blinkAnimation)
            }
        }
    }

    private fun stopAnimation() {

    }
}