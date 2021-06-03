package com.elihimas.orchestra.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.elihimas.orchestra.Orchestra
import com.elihimas.orchestra.OrchestraContext
import com.elihimas.orchestra.R
import kotlinx.android.synthetic.main.activity_fade_example.*
import kotlinx.android.synthetic.main.activity_splash.*

//TODO: implement this
class FadeExampleActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fade_example)

        initAnimation()
    }

    private fun initAnimation() {
        val smallScale = 0.3f
        val fullScale = 1

        Orchestra.setup {
            on(squareLeft, squareCenter, squareRight)
                    .alpha(0)
                    .scale(smallScale)
        }
        Orchestra.launch {
            val duration = 600L
            val blink = animations()
                    .parallel {
                        fadeIn {
                            this.duration = duration
                        }
                        scale(fullScale) {
                            this.duration = duration
                        }
                    }
                    .parallel {
                        fadeOut {
                            this.duration = duration
                        }
                        scale(smallScale) {
                            this.duration = duration
                        }
                    }
                    .delay(600)
            val show = animations()
                    .parallel {
                        scale(1) {
                            this.duration = duration
                        }
                        fadeIn {
                            this.duration = duration
                        }
                    }
            val animations = animations()
                    .repeat(3, blink)
                    .addAnimations(show)

            parallel {
                on(squareLeft).addAnimations(animations)
                on(squareCenter).delay(400).addAnimations(animations.clone())
                on(squareRight).delay(800).addAnimations(animations.clone())
            }
        }
    }

}