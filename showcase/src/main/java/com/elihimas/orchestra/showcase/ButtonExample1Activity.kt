package com.elihimas.orchestra.showcase

import android.os.Bundle
import android.view.animation.AccelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.elihimas.orchestra.Orchestra
import com.elihimas.orchestra.showcase.databinding.ActivityButtonExample1Binding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ButtonExample1Activity : AppCompatActivity() {

    private val binding by lazy { ActivityButtonExample1Binding.inflate(layoutInflater) }

    private val enableDisableView by lazy {
        with(binding) {
            listOf(etLogin, etPassword, btLogin)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initOrchestra()
        initViews()
    }

    private fun initOrchestra() = with(binding) {
        Orchestra.setup {
            on(progressBar).invisible()
            on(tvCode).scale(0f)
        }
    }

    private fun initViews() = with(binding) {
        btLogin.setOnClickListener {
            runButtonAnimation()
        }
    }

    private fun runButtonAnimation() = with(binding) {
        enableDisableView.forEach { view ->
            view.isEnabled = false
        }
        btLogin.text = ""
        
        Orchestra.launch {
            parallel {
                on(btLogin)
                .scale(scaleX = 0.1f) {
                    duration = 300
                    interpolator = AccelerateInterpolator()
                }

                on(btLogin).fadeOut {
                    duration = 200
                    delay = 100
                }
            }

            on(progressBar).fadeIn {
                duration = 100
            }
        }

        reverseAnimationWithDelay()
    }

    private fun reverseAnimationWithDelay() {
        lifecycleScope.launch {
            delay(3_000)
            reverseAnimation()
        }
    }

    private fun reverseAnimation() = with(binding) {
        Orchestra.launch {
            on(progressBar).fadeOut {
                duration = 100
            }

            parallel {
                on(btLogin).fadeIn {
                    duration = 300
                }

                on(btLogin)
                    .scale(scaleX = 1f) {
                        delay = 100
                        duration = 200
                        interpolator = AccelerateInterpolator()
                    }
            }
        }.then {
            enableDisableView.forEach { view ->
                view.isEnabled = true
            }
            btLogin.text = "Login"
        }
    }
}