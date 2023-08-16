package com.elihimas.orchestra.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.elihimas.orchestra.Orchestra
import com.elihimas.orchestra.databinding.ActivityFadeExampleBinding

class FadeExampleActivity : AppCompatActivity() {
    private val binding by lazy { ActivityFadeExampleBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        init()
    }

    private fun init() = with(binding) {
        btStart.setOnClickListener {
            runAnimation()
        }
        btStop.setOnClickListener {
            stopAnimation()
        }
    }

    private fun runAnimation() = with(binding) {
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
                .rotate(180f)

            parallel {
                on(squareLeft).forever(blinkAnimation)
                on(squareCenter).delay(400).forever(blinkAnimation.clone())
                on(squareRight).delay(800).forever(blinkAnimation.clone())
            }
        }
    }

    private fun stopAnimation() {

    }
}