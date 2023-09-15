package com.elihimas.orchestra.showcase.activities

import android.os.Bundle
import android.view.animation.AccelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.elihimas.orchestra.Orchestra
import com.elihimas.orchestra.showcase.R
import com.elihimas.orchestra.showcase.databinding.ActivityAnimatedButtonBinding
import com.elihimas.orchestra.showcase.databinding.TvCodeAndRunningControlsBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AnimatedButtonActivity : AppCompatActivity() {

    private val binding by lazy { ActivityAnimatedButtonBinding.inflate(layoutInflater) }
    private val runControlsBinding by lazy { TvCodeAndRunningControlsBinding.bind(binding.root) }

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
        }

        Orchestra.launch {
            runControlsBinding.runAndCodeControls.fadeInWithContext(this)
        }
    }

    private fun initViews() = with(binding) {
        btLogin.setOnClickListener {
            runButtonAnimation()
        }

        with(runControlsBinding.runAndCodeControls){
            hideRunAnimationButton()
            setCodeText(R.string.code_animated_button)
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
        }.andThen {
            enableDisableView.forEach { view ->
                view.isEnabled = true
            }
            btLogin.setText(R.string.login)
        }
    }
}