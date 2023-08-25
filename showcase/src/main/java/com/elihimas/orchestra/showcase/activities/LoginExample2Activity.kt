package com.elihimas.orchestra.showcase.activities

import android.os.Bundle
import android.view.animation.AccelerateInterpolator
import android.view.animation.AnticipateInterpolator
import android.view.animation.OvershootInterpolator
import androidx.appcompat.app.AppCompatActivity
import com.elihimas.orchestra.Orchestra
import com.elihimas.orchestra.showcase.databinding.ActivityLogin2Binding

class LoginExample2Activity : AppCompatActivity() {

    private val binding by lazy { ActivityLogin2Binding.inflate(layoutInflater) }

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
            on(tvTitle)
                .scale(2f)
                .bottomToTopOf(form)

            on(container).alpha(0)
            on(tvCode).scale(0f)
            on(form).scale(scaleY = 0f)
        }
    }

    private fun runAnimation() = with(binding) {
        Orchestra.launch {
            delay(1_000)
            on(tvTitle).scale(1f) {
                duration = 1_000
                interpolator = AccelerateInterpolator()
            }

            delay(200)

            parallel {
                on(form).scale(1f) {
                    duration = 300
                }

                on(container)
                    .delay(200)
                    .fadeIn {
                        duration = 500
                    }
            }
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
            setupOrchestra()
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


}