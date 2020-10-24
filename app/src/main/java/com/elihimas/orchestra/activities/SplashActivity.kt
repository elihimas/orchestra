package com.elihimas.orchestra.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.elihimas.orchestra.Orchestra
import com.elihimas.orchestra.R
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_splash)

        Orchestra.shortOrLong(::tempShortSplash, ::longSplash)
    }

    private fun tempShortSplash() {
        Orchestra.launch {
            on(titleText)
                    .fadeIn {
                        duration = 5000
                    }
                    .fadeOut {
                        duration = 9000
                    }
        }
    }

    private fun shortSplash() {
        Orchestra.launch {
            delay(500)

            on(titleText)
                    .fadeIn()
                    .fadeOut()
        }.then {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    private fun longSplash() {
        Orchestra.launch {
            val showAnimation = animations().fadeIn()
            val hide = animations().parallel {
                scale(1f)
                fadeOut()
            }

            on(titleText)
                    .addAnimations(showAnimation)

            on(titleText)
                    .scale(2f)
                    .addAnimations(hide)

            parallel {
                on(leftSquare)
                        .scale(5f)

                on(rightSquare)
                        .scale(5f)
            }

            on(titleText)
                    .scale(1f)

            parallel {
                on(leftSquare)
                        .scale(1f)
                        .scale(2f)
                        .scale(1f)

                on(rightSquare)
                        .scale(1f)
            }

            parallel {
                on(leftSquare)
                        .translate(300f, 0f)
                        .translate(0f, 0f)

                on(rightSquare)
                        .translate(-300f, 0f)
                        .translate(0f, 0f)
            }

            parallel {
                on(titleText)
                        .scale(3f)

                on(leftSquare)
                        .scale(3f)

                on(rightSquare)
                        .scale(3f)
            }
        }.then {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

}

