package com.elihimas.orchestra.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.marginRight
import com.elihimas.orchestra.Orchestra
import com.elihimas.orchestra.R
import com.elihimas.orchestra.animations.Direction
import kotlinx.android.synthetic.main.activity_splash.*


class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_splash)

//       addClick()
        longSplash()
    }

    private fun addClick() {
        listOf(sqr1, sqr2, sqr3, sqr4, sqr5, sqr6, sqr7, sqr8, sqr9, sqr10).forEach {
            it.setOnClickListener {
                Orchestra.launch {
                    on(it).rotate(1080f) {
                        duration = 120
                    }
                }
            }
        }
    }

    private fun longSplash() {
        Orchestra.setup {
            on(titleText).alpha(0)
        }
        root.post {
            Orchestra.launch {
                val showAnimation = animations()
                        .delay(200)
                        .fadeIn() {
                            duration = 1200
                        }
                        .scale(1.5f) {
                            duration = 300
                        }
                        .delay(600)
                        .scale(1f)
                        .fadeOut()

                on(titleText)
                        .addAnimations(showAnimation)

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
                        .fadeIn {
                            duration = 800
                        }
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
                on(rightSquare)
                        .circularReveal()
                        .delay(2000)
                        .scale(4)
                        .rotate(3600f) {
                            duration = 5000
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

                val pulseAnimation1 =
                        animations()
                                .scaleY(12, Direction.Up) {
                                    duration = 300
                                }
                                .delay(100)
                                .scaleY(2, Direction.Up) {
                                    duration = 300
                                }
                                .delay(250)
                                .scaleY(6, Direction.Up) {
                                    duration = 320
                                }
                                .delay(100)
                                .scaleY(0.5f, Direction.Up) {
                                    duration = 300
                                }

                val pulseAnimation2 = animations()
                        .scaleY(8, Direction.Up) {
                            duration = 200
                        }
                        .scaleY(1, Direction.Up) {
                            duration = 200
                        }
                        .scaleY(8, Direction.Up) {
                            duration = 200
                        }
                        .scaleY(1, Direction.Up) {
                            duration = 200
                        }
                        .scaleY(8, Direction.Up) {
                            duration = 200
                        }
                        .scaleY(1, Direction.Up) {
                            duration = 200
                        }
                        .scaleY(8, Direction.Up) {
                            duration = 200
                        }
                        .scaleY(1, Direction.Up) {
                            duration = 200
                        }
                val pulseAnimation3 = animations()
                        .scaleY(7, Direction.Up) {
                            duration = 350
                        }
                        .scaleY(0.8f, Direction.Up) {
                            duration = 450
                        }
                        .scaleY(7, Direction.Up) {
                            duration = 300
                        }
                        .scaleY(1.2f, Direction.Up) {
                            duration = 400
                        }
                        .scaleY(7, Direction.Up) {
                            duration = 300
                        }
                        .scaleY(1.6f, Direction.Up) {
                            duration = 400
                        }

                delay(600)

                parallel {
                    on(bottomSquareLeft)
                            .addAnimations(pulseAnimation1)
                    on(bottomSquareCenter)
                            .addAnimations(pulseAnimation2)
                    on(bottomSquareRight)
                            .addAnimations(pulseAnimation3)
                }

                val sqr1Translation = bottomSquareLeft.marginRight + bottomSquareLeft.width
                val sqr3Translation = (bottomSquareRight.marginRight + bottomSquareRight.width) * -1

                parallel {
                    on(bottomSquareLeft).translate(sqr1Translation.toFloat(), 0.0f) {
                        duration = 800
                    }

                    on(bottomSquareRight).translate(sqr3Translation.toFloat(), 0.0f) {
                        duration = 800
                    }
                }

                delay(300)

                on(bottomSquareLeft,
                        bottomSquareCenter,
                        bottomSquareRight)
                        .fadeOut() {
                            duration = 800
                        }
            }
                    .then {
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    }
        }
    }

}

