package com.elihimas.orchestra.showcase.activities

import android.content.Intent
import android.os.Bundle
import android.view.animation.AccelerateInterpolator
import android.view.animation.OvershootInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.marginRight
import com.elihimas.orchestra.Orchestra
import com.elihimas.orchestra.animations.Direction
import com.elihimas.orchestra.showcase.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {

    private val binding by lazy { ActivitySplashBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        longSplash()
    }

    private fun longSplash() = with(binding) {
        Orchestra.setup {
            on(titleText).alpha(0)
        }
        root.post {
            Orchestra.launch {
                val showAnimation = createAnimation()
                    .delay(200)
                    .fadeIn() {
                        duration = 1200
                    }
                    .scale(2.5f) {
                        duration = 400
                        interpolator = OvershootInterpolator()
                    }
                    .delay(600)
                    .parallel {
                        scale(1f) {
                            duration = 1100
                        }
                        fadeOut {
                            duration = 1000
                        }
                    }

                on(titleText)
                    .addAnimations(showAnimation)

                parallel {
                    on(sqr1)
                        .rotate(180f) {
                            duration = 300
                        }
                        .scale(2f) {
                            duration = 400
                        }

                    on(sqr3)
                        .scale(2f) {
                            duration = 400
                        }
                        .rotate(180f) {
                            duration = 300
                        }

                    on(sqr5)
                        .scale(2f) {
                            duration = 600
                        }
                        .rotate(720f) {
                            duration = 800
                        }
                }

                on(sqr1, sqr3, sqr5)
                    .scale(1f) {
                        duration = 300
                    }


                on(titleText)
                    .fadeIn {
                        duration = 800
                    }
                    .scale(3f) {
                        duration = 700
                        interpolator = AccelerateInterpolator()
                    }

                val pulseAnimation1 =
                    createAnimation()
                        .scale(12, Direction.Up) {
                            duration = 300
                        }
                        .delay(100)
                        .scale(2, Direction.Up) {
                            duration = 300
                        }
                        .delay(250)
                        .scale(6, Direction.Up) {
                            duration = 320
                        }
                        .delay(100)
                        .scale(0.5f, Direction.Up) {
                            duration = 300
                        }

                val pulseAnimation2 = createAnimation()
                    .scale(8, Direction.Up) {
                        duration = 200
                    }
                    .scale(1, Direction.Up) {
                        duration = 200
                    }
                    .scale(8, Direction.Up) {
                        duration = 200
                    }
                    .scale(1, Direction.Up) {
                        duration = 200
                    }
                    .scale(8, Direction.Up) {
                        duration = 200
                    }
                    .scale(1, Direction.Up) {
                        duration = 200
                    }
                    .scale(8, Direction.Up) {
                        duration = 200
                    }
                    .scale(1, Direction.Up) {
                        duration = 200
                    }
                val pulseAnimation3 = createAnimation()
                    .scale(7, Direction.Up) {
                        duration = 350
                    }
                    .scale(0.8f, Direction.Up) {
                        duration = 450
                    }
                    .scale(7, Direction.Up) {
                        duration = 300
                    }
                    .scale(1.2f, Direction.Up) {
                        duration = 400
                    }
                    .scale(7, Direction.Up) {
                        duration = 300
                    }
                    .scale(1.6f, Direction.Up) {
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
                    on(bottomSquareLeft).translate(sqr1Translation.toFloat(), 0f) {
                        duration = 800
                    }

                    on(bottomSquareRight).translate(sqr3Translation.toFloat(), 0f) {
                        duration = 800
                    }
                }

                delay(300)

                on(
                    bottomSquareLeft,
                    bottomSquareCenter,
                    bottomSquareRight
                )
                    .fadeOut {
                        duration = 800
                    }
            }
                .then {
                    startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                    finish()
                }
        }
    }
}