package com.elihimas.orchestra.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.elihimas.orchestra.*
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_splash)

        init()
    }

    private fun init() {
        listOf(sqr1, sqr2, sqr3, sqr4, sqr5, sqr6, sqr7, sqr8, sqr9, sqr10).forEachIndexed { index, view ->
            view.setOnClickListener {

                Orchestra.launch {
                    on(titleText)
                            .scale(4) { duration = 300 }
                            .changeTextColor(
                                    android.R.color.holo_red_light,
                                    android.R.color.holo_blue_light,
                                    android.R.color.holo_orange_dark,
                                    android.R.color.holo_orange_light,
                                    android.R.color.holo_green_light)
                            {
                                duration = 2600
                            }
                            .scale(1) { duration = 300 }


                    parallel {
                        on(view)
                                .changeBackground(
                                        android.R.color.holo_green_dark,
                                        android.R.color.holo_orange_dark,
                                        android.R.color.holo_red_dark,
                                        android.R.color.holo_orange_light){
                                    duration = 3000
                                }

                        on(sqr5)
                                .changeBackground(
                                        android.R.color.black,
                                        android.R.color.holo_green_light,
                                        android.R.color.darker_gray,
                                        android.R.color.transparent,
                                        android.R.color.holo_purple){
                                    duration = 3000
                                }
                    }
                }
            }
        }

        //Orchestra.shortOrLong(::tempShortSplash, ::longSplash)
    }

    private fun tempShortSplash() {
        Orchestra.launch {
            parallel {
                on(sqr1)
                        .rotate(180f) {
                            duration = 300
                        }
                        .scale(2) {
                            duration = 400
                        }


                on(sqr3)
                        .scale(2) {
                            duration = 400
                        }
                        .rotate(180f) {
                            duration = 300
                        }


                on(sqr5)
                        .scale(2) {
                            duration = 600
                        }
                        .rotate(720f) {
                            duration = 800
                        }
            }

            on(sqr1, sqr3, sqr5)
                    .scale(1) {
                        duration = 300
                    }

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

        android.os.Handler().postDelayed({
            Orchestra.launch {
                on(rightSquare)
                        .scale(4)
                        .rotate(3600f) {
                            duration = 10000
                        }
            }
        }, 2200)
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

