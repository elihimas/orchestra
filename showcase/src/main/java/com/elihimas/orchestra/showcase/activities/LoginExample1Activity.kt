package com.elihimas.orchestra.showcase.activities

import android.os.Bundle
import android.view.animation.AnticipateInterpolator
import android.view.animation.OvershootInterpolator
import androidx.appcompat.app.AppCompatActivity
import com.elihimas.orchestra.Orchestra
import com.elihimas.orchestra.animations.Direction
import com.elihimas.orchestra.showcase.databinding.ActivityLogin1Binding

class LoginExample1Activity : AppCompatActivity() {

    private val binding by lazy { ActivityLogin1Binding.inflate(layoutInflater) }

    private val showButtonAlpha = .5f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupOrchestra()
        setupViews()

        runAnimation()
    }

    private fun setupOrchestra() = with(binding) {
        Orchestra.setup {
            on(tvCode).scale(0f)
        }
    }

    private fun setupViews() = with(binding) {
        btShowCode.setOnClickListener {
            showCode()
        }
        btHideCode.setOnClickListener {
            hideCode()
        }
        btRun.setOnClickListener {
            runAnimation()
        }
    }

    private fun showCode() = with(binding) {
        Orchestra.launch {
            parallel {
                on(tvCode).scale(1f) {
                    duration = 600
                    interpolator = OvershootInterpolator()
                }
                on(btHideCode).fadeIn {
                    finalAlpha = showButtonAlpha
                }
                on(btShowCode, btRun).fadeOut()
            }
        }
    }

    private fun hideCode() = with(binding) {
        Orchestra.launch {
            parallel {
                on(tvCode).scale(0f) {
                    duration = 600
                    interpolator = AnticipateInterpolator()
                }
                on(btHideCode).fadeOut()
                on(btShowCode, btRun).fadeIn {
                    finalAlpha = showButtonAlpha
                }
            }
        }
    }

    private fun runAnimation() = with(binding) {
        val slidingViews = arrayOf(
            titleText,
            loginText,
            loginEditText,
            passwordText,
            passwordEditText,
            loginButton
        )

        Orchestra.setup {
            on(form, *slidingViews).invisible()
            on(btShowCode, btHideCode, btRun).alpha(0f)
        }

        Orchestra.launch {
            delay(500)

            parallel {
                on(form).parallel {
                    slide(Direction.Up) {
                        duration = 600
                    }
                    fadeIn {
                        duration = 300
                    }
                }

                on(*slidingViews)
                    .delay(300)
                    .slide(Direction.Up) {
                        duration = 600
                    }
            }

            on(btShowCode, btRun)
                .fadeIn {
                    finalAlpha = showButtonAlpha
                }
        }
    }


}