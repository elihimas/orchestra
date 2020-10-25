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

        init()
    }

    private fun init() {
        listOf(sqr1, sqr2, sqr3, sqr4, sqr5).forEach {
            it.setOnClickListener {
                Orchestra.launch {
                    on(it)
                            .rotate(90f) {
                                duration = 2000
                            }
                }
            }
        }

        root.setOnClickListener {
            onRootClicked()
        }

        Orchestra.shortOrLong(::tempShortSplash, ::longSplash)
    }

    private fun onRootClicked() {
        Orchestra.launch {
            on(rightSquare)
                    .scale(6) {
                        duration = 700
                    }
                    .rotate(-360f) {
                        duration = 6000
                    }
        }
    }

    private fun tempShortSplash() {
        Orchestra.launch {
            on(titleText, leftSquare)
                    .fadeIn {
                        duration = 800
                    }
                    .scale(3) {
                        duration = 2000
                    }
                    .rotate(180f) {
                        duration = 700
                    }
                    .rotate(-360f) {
                        duration = 900
                    }
                    .rotate(180f) {
                        duration = 700
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

